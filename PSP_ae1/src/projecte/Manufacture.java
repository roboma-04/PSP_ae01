package projecte;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe Manufacture: Gestiona la fabricació de tetrominos amb màquines virtuals.
 */
public class Manufacture {
	private static final int MAX_MAQUINES = 8;
    private static final List<String> pecesFabricades = new ArrayList<>();
    private static int pecesEnProcés = 0;

   
    /**
     * Mètode per fabricar tetrominos amb les especificacions donades.
     * @param tipusTetromino Tipus de tetromino a fabricar.
     * @param quantitat Quantitat de tetrominos a fabricar.
     * @param mostrarEnConsola Indica si es mostrarà la informació per consola.
     */
    public static void fabricarTetrominos(String tipusTetromino, int quantitat, boolean mostrarEnConsola) {
        for (int i = 0; i < quantitat; i++) {
            if (pecesFabricades.size() < MAX_MAQUINES) {
            	// Incrementa el comptador
                pecesEnProcés++; 
                // Crea un fil per fabricar la peça
                Thread t = new Thread(new TetrominoRunnable(tipusTetromino, mostrarEnConsola));
                t.start();
            } else {
                esperarFinsHiHagiMaquinaDisponible();
                i--;
            }
        }

        // Espera a que es fabriquen totes les peces abans de continuar
        esperarFinsFinalitzinTotesLesPeces();

        // Restaura el comptador
        pecesEnProcés = 0;

        // Guarda l'ordre de fabricació en un fitxer només si s'ha fabricat alguna peça
        if (!pecesFabricades.isEmpty()) {
            guardarOrdreEnFitxer(tipusTetromino);
        }

        // Imprimeix la llista de peces fabricades només si s'ha fabricat alguna peça i mostrarEnConsola és true
        if (!pecesFabricades.isEmpty() && mostrarEnConsola) {
            imprimirPecesFabricades();
        }
    }

    /**
     * Espera fins que totes les peces estiguin fabricades i el procés hagi finalitzat.
     */
    private static void esperarFinsFinalitzinTotesLesPeces() {
        synchronized (pecesFabricades) {
            while (pecesEnProcés > 0) {
                try {
                    pecesFabricades.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // S'afegeix una notificació extra per assegurar-se que altres fils es desperten.
            pecesFabricades.notifyAll();
        }
    }

   /**
    * Espera fins que hi hagi una màquina disponible per fabricar una nova peça.
    */
    private static void esperarFinsHiHagiMaquinaDisponible() {
        // Lògica per esperar fins que hi hagi màquines disponibles
        synchronized (pecesFabricades) {
            while (pecesFabricades.size() >= MAX_MAQUINES) {
                try {
                    pecesFabricades.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Imprimeix les peces fabricades per la consola.
     */
    private static void imprimirPecesFabricades() {
        // Imprimeix les peces fabricades
        for (String peça : pecesFabricades) {
            System.out.println(peça);
        }
    }

    /**
     * Notifica que hi ha màquines disponibles per fabricar més peces.
     */
    private static void notificarMaquinaDisponible() {
        // Lògica per notificar que hi ha màquines disponibles
        synchronized (pecesFabricades) {
            pecesFabricades.notify();
        }
    }

    /**
     * Afegeix una peça fabricada a la llista global de peces.
     * @param tipusTetromino Tipus de tetromino que s'ha fabricat.
     */
    private static void afegirAListaPecesFabricades(String tipusTetromino) {
        // Lògica per afegir a la llista global de peces fabricades
        synchronized (pecesFabricades) {
            Date dataActual = new Date();
            SimpleDateFormat formatData = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String marcaTemps = formatData.format(dataActual);

            String peçaString = tipusTetromino + "_" + marcaTemps;
            pecesFabricades.add(peçaString);

            System.out.println(peçaString);
            notificarMaquinaDisponible();
        }
    }

    /**
     * Classe interna per executar la lògica de fabricació d'un tetromino en un fil separat.
     */
    static class TetrominoRunnable implements Runnable {
        private final String tipusTetromino;
        private final boolean mostrarEnConsola;

        /**
         * Constructor de la classe TetrominoRunnable.
         * @param tipusTetromino Tipus de tetromino que es fabricarà.
         * @param mostrarEnConsola Booleà que indica si s'ha de mostrar informació per la consola.
         */
        TetrominoRunnable(String tipusTetromino, boolean mostrarEnConsola) {
            this.tipusTetromino = tipusTetromino;
            this.mostrarEnConsola = mostrarEnConsola;
        }

        
        /**
         * Mètode run que s'executa quan s'inicia el fil per fabricar un tetromino.
         */
        @Override
        public void run() {
            // Lògica per fabricar un tetromino
            int tempsFabricacio = obtenirTempsFabricacio(tipusTetromino);
            procesFabricacio(tempsFabricacio);

            // Agrega la pieza fabricada a la lista global
            afegirAListaPecesFabricades(tipusTetromino);

            synchronized (pecesFabricades) {
                // Decrementa el comptador
            	pecesEnProcés--; 
                if (pecesEnProcés == 0) {
                	// Notifica quan s'han completat totes les peces
                    pecesFabricades.notifyAll();
                }
            }

            if (mostrarEnConsola) {
                // Pots afegir lògica addicional aquí que depenga de mostrarEnConsola
                System.out.println("Peça fabricada: " + tipusTetromino);
            }
        }

        /**
         * Mètode per obtenir el temps de fabricació segons el tipus de tetromino.
         * @param tipusTetromino Tipus de tetromino del qual s'obtindrà el temps de fabricació.
         * @return Enter que representa el temps de fabricació en mil·lisegons.
         */
        private int obtenirTempsFabricacio(String tipusTetromino) {
            // Lògica per obtenir el temps de fabricació segons el tipus de tetromino
            switch (tipusTetromino) {
                case "I":
                    return 1000;
                case "O":
                    return 2000;
                case "T":
                    return 3000;
                case "J":
                case "L":
                    return 4000;
                case "S":
                case "Z":
                    return 5000;
                default:
                    return 0;
            }
        }
    }

    /**
     * Mètode per simular el procés de fabricació ocupant el processador durant un temps determinat.
     * @param tempsFabricacio Temps de fabricació en mil·lisegons.
     */
    private static void procesFabricacio(int tempsFabricacio) {
        long tempsInici = System.currentTimeMillis();
        long tempsFinal = tempsInici + tempsFabricacio;
        // Realitzar iteracions addicionals per consumir processador (simula ocupació de màquina)
        while (System.currentTimeMillis() < tempsFinal) {
        }
    }

    /**
     * Mètode per desar l'ordre de fabricació en un fitxer de text.
     * @param tipusTetromino Tipus de tetromino de l'ordre.
     */
    private static void guardarOrdreEnFitxer(String tipusTetromino) {
        synchronized (pecesFabricades) {
            Date dataActual = new Date();
            SimpleDateFormat formatData = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String marcaTemps = formatData.format(dataActual);

            String nomFitxer = "LOG_" + marcaTemps + ".txt";

            try (FileWriter escriptor = new FileWriter(nomFitxer)) {
                for (String peça : pecesFabricades) {
                    escriptor.write(peça + "\n");
                }
                System.out.println("Fitxer guardat a: " + nomFitxer);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error al guardar el fitxer.");
            }
        }
    }

    /**
     * Mètode per desar totes les ordres de fabricació en un fitxer de text.
     * @param directori Directori on es desara el fitxer.
     */
    public static void guardarTotesLesOrdresEnFitxer(String directori) {
        synchronized (pecesFabricades) {
            Date dataActual = new Date();
            SimpleDateFormat formatData = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String marcaTemps = formatData.format(dataActual);

            String nomFitxer = directori + "/LOG_" + marcaTemps + ".txt";

            try (FileWriter escriptor = new FileWriter(nomFitxer)) {
                for (String peça : pecesFabricades) {
                    escriptor.write(peça + "\n");
                }
                System.out.println("Fitxer guardat a: " + nomFitxer);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error al guardar el fitxer.");
            }
        }
    }
}
