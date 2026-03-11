package com.finanzas.web.util;

public final class RncValidatorUtil {
    private RncValidatorUtil() {}

    public static boolean validarRNC(String rnc) {
        if (rnc == null) return false;
        rnc = rnc.trim();
        if (rnc.length() != 9) return false;

        int[] peso = {7, 9, 8, 6, 5, 4, 3, 2};
        int suma = 0;

        for (int i = 0; i < 8; i++) {
            char ch = rnc.charAt(i);
            if (!Character.isDigit(ch)) return false;
            suma += Character.getNumericValue(ch) * peso[i];
        }

        int division = suma / 11;
        int resto = suma - (division * 11);

        int digito;
        if (resto == 0) digito = 2;
        else if (resto == 1) digito = 1;
        else digito = 11 - resto;

        return digito == Character.getNumericValue(rnc.charAt(8));
    }
}