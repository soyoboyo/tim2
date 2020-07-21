import { Injectable } from '@angular/core';
import { Project } from '../../shared/types/entities/Project';

@Injectable({
	providedIn: 'root'
})
export class ProjectsStoreService {

	private selectedProject: Project = null;

	getSelectedProject() {
		return this.selectedProject;
	}

	setSelectedProject(project: Project) {
		this.selectedProject = project;
	}

}
