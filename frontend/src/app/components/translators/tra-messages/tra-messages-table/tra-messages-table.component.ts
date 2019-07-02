import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Project } from '../../../../models/entities/Project';
import { Message } from '../../../../models/entities/Message';
import { MessageForTranslator } from '../../../../models/MessageForTranslator';

@Component({
	selector: 'app-tra-messages-table',
	templateUrl: './tra-messages-table.component.html',
	styleUrls: ['./tra-messages-table.component.scss', '../tra-messages.component.scss'],
	animations: [
		trigger('detailExpand', [
			state('collapsed', style({ height: '0px', minHeight: '0' })),
			state('expanded', style({ height: '*' })),
			transition('* => collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('expanded => *', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('* => expanded', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('collapsed => *', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
		])
	]
})

export class TraMessagesTableComponent implements OnInit, OnChanges {

	@Input() selectedRowIndex = -1;
	@Input() selectedProject: Project;
	@Input() messages: Message[] = [];
	@Output() sendSelectedLocale = new EventEmitter<any>();
	@Output() addTranslationEvent = new EventEmitter<any>();
	@Output() editTranslationEvent = new EventEmitter<any>();
	@Output() invalidateTranslationEvent = new EventEmitter<any>();

	// table
	@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
	dataSource = new MatTableDataSource<MessageForTranslator>();
	displayedColumns: string[] = ['key', 'content'];
	@ViewChild(MatSort, { static: true }) sort: MatSort;
	isLoadingResults = false;

	expandedRow = null;
	selectedLocale: string = null;

	constructor() {
	}

	ngOnInit() {
		this.getMessages();
	}

	ngOnChanges(changes: SimpleChanges): void {
		this.getMessages();
	}

	async getMessages() {
		this.isLoadingResults = true;
		this.createDataSource(this.messages);
		this.isLoadingResults = false;
	}

	addTranslation(message: any) {
		this.selectedRowIndex = message.id;
		this.addTranslationEvent.emit(message);
	}

	updateTranslation(message: any) {
		this.selectedRowIndex = message.id;
		this.editTranslationEvent.emit(message);
	}

	applyFilter(filterValue: string) {
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

	createDataSource(messages: any[]) {
		this.dataSource = new MatTableDataSource(messages);
		this.dataSource.paginator = this.paginator;
		this.dataSource.filterPredicate = (data, filter: string) => {
			return JSON.stringify(data).toLowerCase().includes(filter.toLowerCase());
		};
		this.dataSource.sort = this.sort;
	}

	selectLocale(value: string) {
		if (!this.displayedColumns.includes('actions')) {
			this.displayedColumns.push('actions');
		}
		this.selectedLocale = value;
		this.sendSelectedLocale.emit(value);
	}

	async invalidateTranslation(message: any) {
		this.invalidateTranslationEvent.emit(message);
	}

	isTranslationOutdated(message: MessageForTranslator): boolean {
		if(message.translation !== null){
			return message.updateDate > message.translation.updateDate;
		} else {
			return false;
		}
	}

}
