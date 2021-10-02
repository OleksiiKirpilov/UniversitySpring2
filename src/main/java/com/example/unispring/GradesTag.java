package com.example.unispring;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom tag class for faculty user form. The main purpose is
 * to remove the need of validation in ApplyFacultyView command.
 * The maximum grade in subject is equal to 12, the lower one is zero.
 */
@Getter
@Setter
public class GradesTag extends SimpleTagSupport {

    private static final List<Integer> grades = new ArrayList<>();

    private int subjectId;
    private String examType;

    static {
        for (int i = 0; i <= 12; i++) {
            grades.add(i);
        }
    }

    @Override
    public void doTag() throws IOException {
        JspWriter out = getJspContext().getOut();
        out.println("<select name=\"" + subjectId + "_" + examType + "\">");
        for (Integer grade : grades) {
            out.println("<option>" + grade + "</option>");
        }
        out.println("</select>");
    }

}
