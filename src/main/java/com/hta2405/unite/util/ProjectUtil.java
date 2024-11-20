package com.hta2405.unite.util;

import com.hta2405.unite.dao.ProjectDAO;

public class ProjectUtil {
	public static String getProjectName(int projectId) {
        ProjectDAO projectDAO = new ProjectDAO();
        return projectDAO.getProjectName(projectId);
    }
}
