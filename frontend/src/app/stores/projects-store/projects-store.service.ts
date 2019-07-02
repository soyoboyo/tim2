import { Injectable } from '@angular/core';
import { Project } from '../../shared/types/entities/Project';

@Injectable({
	providedIn: 'root'
})
export class ProjectsStoreService {

	private selectedProject: Project = null;

	getSelectedProject() {
		if (this.selectedProject !== null) {
			return this.selectedProject;
		} else {
			return null;
		}
	}

	setSelectedProject(project: Project) {
		this.selectedProject = project;
	}

}
