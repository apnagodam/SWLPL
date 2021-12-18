package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmpFeedbackRequest {

    @SerializedName("designation_id")
    @Expose
    private String designation_id;
    @SerializedName("dept_problem")
    @Expose
    private String dept_problem;
    @SerializedName("problem_solution")
    @Expose
    private String problem_solution;
    public EmpFeedbackRequest(String designation_id, String dept_problem, String problem_solution) {
        this.designation_id = designation_id;
        this.dept_problem = dept_problem;
        this.problem_solution = problem_solution;
    }
}


