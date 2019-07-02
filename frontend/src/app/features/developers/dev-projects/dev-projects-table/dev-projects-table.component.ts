import {
	Component,
	EventEmitter,
	Input,
	OnChanges,
	OnInit,
	Output,
	SimpleChanges, ViewChild,
} from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { Project } from '../../../../shared/types/entities/Project';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
	selector: 'app-dev-projects-table',
	templateUrl: './dev-projects-table.component.html',
	styleUrls: ['./dev-projects-table.component.scss', '../dev-projects.component.scss'],
	animations: [
		trigger('detailExpand', [
			state('collapsed', style({ height: '0', minHeight: '0' })),
			state('expanded', style({ height: '*' })),
			transition('* => collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('expanded => *', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('* => expanded', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('collapsed => *', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
		])
	]
})
export class DevProjectsTableComponent implements OnInit, OnChanges {

	@Input() selectedRowIndex = -1;
	@Input() projects: Project[] = [];
	@Input() isLoadingResults = true;
	@Output() editEvent = new EventEmitter<any>();
	@Output() removeEvent = new EventEmitter<any>();
	@Output() formToggle = new EventEmitter<boolean>();

	// table
	@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
	dataSource = new MatTableDataSource<Project>();

	displayedColumns: string[] = ['name', 'sourceLocale', 'actions'];
	@ViewChild(MatSort, { static: true }) sort: MatSort;

	expandedRow = null;

	constructor() {
	}

	ngOnInit() {
		this.getProjects();
	}

	ngOnChanges(changes: SimpleChanges): void {
		this.getProjects();
	}

	async getProjects() {
		this.isLoadingResults = true;
		this.dataSource = new MatTableDataSource(this.projects);
		this.dataSource.paginator = this.paginator;
		this.dataSource.filterPredicate = (data, filter: string) => {
			return JSON.stringify(data).toLowerCase().includes(filter.toLowerCase());
		};
		this.dataSource.sort = this.sort;
		this.isLoadingResults = false;
	}

	async removeProject(id: any) {
		this.selectedRowIndex = id;
		this.removeEvent.emit(id);
	}

	editProject(project: any) {
		this.selectedRowIndex = project.id;
		this.editEvent.emit(project);
	}

	applyFilter(filterValue: string) {
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

	toggleForm() {
		this.formToggle.emit(true);
	}

}
