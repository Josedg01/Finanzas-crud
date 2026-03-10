package com.finanzas.web.validator;

import com.finanzas.web.util.CedulaUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("cedulaValidator")
public class CedulaJSFValidator implements Validator<Object> {

    // AGREGADO: validator JSF para usarlo directamente en los xhtml
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        String cedula = value.toString();

        if (!CedulaUtil.validarCedula(cedula)) {
            throw new ValidatorException(
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Cédula inválida",
                    "La cédula ingresada no cumple con el algoritmo de validación."
                )
            );
        }
    }
}