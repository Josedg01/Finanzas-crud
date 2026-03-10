package com.finanzas.web.util;

public class CedulaUtil {

    // AGREGADO: método reutilizable para validar la cédula con el algoritmo indicado
    public static boolean validarCedula(String pCedula) {
        if (pCedula == null || pCedula.trim().isEmpty()) {
            return false;
        }

        int vnTotal = 0;
        String vcCedula = pCedula.replace("-", "").trim();
        int pLongCed = vcCedula.length();
        int[] digitoMult = new int[]{1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1};

        if (pLongCed != 11) {
            return false;
        }

        // AGREGADO: valida que solo tenga números
        if (!vcCedula.matches("\\d{11}")) {
            return false;
        }

        for (int vDig = 1; vDig <= pLongCed; vDig++) {
            int vCalculo = Integer.parseInt(vcCedula.substring(vDig - 1, vDig)) * digitoMult[vDig - 1];

            if (vCalculo < 10) {
                vnTotal += vCalculo;
            } else {
                vnTotal += (vCalculo / 10) + (vCalculo % 10);
            }
        }

        return vnTotal % 10 == 0;
    }
}