package org.proha.tags;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

public class FormField extends TagSupport {
    private String label;
    private String id;
    private String name;
    private String value;
    private String error;

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write("<div class='form-group'>");
            pageContext.getOut().write("<label for='" + id + "'>" + label + "</label>");
            pageContext.getOut().write("<input type='text' id='" + id + "' name='" + name + "' value='" + (value != null ? value : "") + "' class='form-control'/>");

            if (error != null && !error.isEmpty()) {
                pageContext.getOut().write("<span class='error'>" + error + "</span>");
            }

            pageContext.getOut().write("</div>");
        } catch (IOException e) {
            throw new JspException("Error in formField tag", e);
        }
        return SKIP_BODY;
    }
}
