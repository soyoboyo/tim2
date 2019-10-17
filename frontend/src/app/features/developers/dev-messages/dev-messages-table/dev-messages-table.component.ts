import { AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Project } from '../../../../shared/types/entities/Project';
import { AuditTranslationService } from '../../dev-history-translations/audit-translation/audit-translation.service';
import { AuditMessageService } from '../../dev-history-messages/audit-message/audit-message.service';
import { MessageForDeveloper } from '../../../../shared/types/DTOs/output/MessageForDeveloper';

@Component({
	selector: 'app-dev-messages-table',
	templateUrl: './dev-messages-table.component.html',
	styleUrls: ['./dev-messages-table.component.scss', '../dev-messages.component.scss'],
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
export class DevMessagesTableComponent implements OnInit, OnChanges, AfterViewInit {

	@Input() selectedRowIndex = -1;
	@Input() selectedProject: Project;
	@Input() messages: MessageForDeveloper[] = [];
	@Output() editEvent = new EventEmitter<any>();
	@Output() archiveEvent = new EventEmitter<any>();
	@Output() formToggle = new EventEmitter<boolean>();

	// table
	@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
	dataSource = new MatTableDataSource<MessageForDeveloper>([]);
	displayedColumns: string[] = ['key', 'content', 'translations', 'actions'];
	@ViewChild(MatSort, { static: true }) sort: MatSort;
	isLoadingResults = false;

	expandedRow = null;
	auditedTranslation = null;
	auditedMessage = null;

	sortAndPaginatorId = 'sortAndPaginator';
	sortAndPaginatorElement: any;

	constructor(private auditTranslationService: AuditTranslationService,
				private auditMessageService: AuditMessageService) {
	}

	ngOnInit() {


		this.getMessages();
	}

	ngOnChanges(changes: SimpleChanges): void {
		this.getMessages();
	}


	async getMessages() {
		this.isLoadingResults = true;
		this.dataSource = new MatTableDataSource(this.messages);
		this.dataSource.paginator = this.paginator;
		this.dataSource.filterPredicate = (data, filter: string) => {
			return JSON.stringify(data).toLowerCase().includes(filter.toLowerCase());
		};
		this.dataSource.sort = this.sort;
		this.isLoadingResults = false;
		if (this.sortAndPaginatorElement != undefined) {
			this.showElement(this.sortAndPaginatorElement);
		}
	}

	archiveMessage(id: any) {
		this.selectedRowIndex = id;
		this.archiveEvent.emit(id);
	}

	editMessage(message: any) {
		this.selectedRowIndex = message.id;
		this.editEvent.emit(message);
	}

	applyFilter(filterValue: string) {
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

	toggleForm() {
		this.formToggle.emit(true);
	}

	auditTranslation(translation: any) {
		this.auditTranslationService.auditedTranslation = translation;
	}

	auditMessage(message: any) {
		this.auditMessageService.auditedMessage = message;
	}


	ngAfterViewInit(): void {
		this.sortAndPaginatorElement = document.getElementById(this.sortAndPaginatorId);
		this.hideElement(this.sortAndPaginatorElement);
	}

	switchOpenHideElement(element: any) {
		if (element.style.display === 'none') {
			element.style.display = 'block';
		} else {
			element.style.display = 'none';
		}
	}

	hideElement(element: any) {
		element.style.display = 'none';

	}

	showElement(element: any) {
		element.style.display = 'block';
	}
}
