import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Project } from '../../../../shared/types/entities/Project';
import { Message } from '../../../../shared/types/entities/Message';
import { MessageForTranslator } from '../../../../shared/types/DTOs/output/MessageForTranslator';
import { log } from 'util';

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
	@Input() selectedLocale: string = null;
	@Input() messages: Message[] = [];
	@Output() sendSelectedLocale = new EventEmitter<any>();
	@Output() addTranslationEvent = new EventEmitter<any>();
	@Output() editTranslationEvent = new EventEmitter<any>();
	@Output() invalidateTranslationEvent = new EventEmitter<any>();

	// table
	@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
	dataSource = new MatTableDataSource<MessageForTranslator>();
	displayedColumns: string[] = ['index', 'content', 'existing', 'upToDate', 'valid'];
	@ViewChild(MatSort, { static: true }) sort: MatSort;
	isLoadingResults = false;

	expandedRow = null;
	missing = false;
	outdated = false;
	invalid = false;

	constructor() {
	}

	ngOnInit() {
		this.getMessages();
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (this.selectedLocale !== null) {
			if (!this.displayedColumns.includes('actions')) {
				this.displayedColumns = ['index', 'content', 'existing', 'upToDate', 'valid', 'actions'];
			}
			this.getMessages();
			this.dataSource.filter = '{';
		}
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
			return JSON.stringify(data).toLowerCase().includes(filter.toLowerCase())
				&& this.filterByMissing(data)
				&& this.filterByInvalid(data)
				&& this.filterByOutdated(data);
		};
		this.dataSource.sort = this.sort;
	}

	filterByMissing(row): boolean {
		if (this.missing === false) {
			return true;
		} else {
			if (row.translation === null) {
				return true;
			}
		}
		return false;
	}

	filterByInvalid(row): boolean {
		if (this.invalid === false) {
			return true;
		} else {
			if (row.translation !== null) {
				if (row.translation.isValid === false) {
					return true;
				}
			}
		}
		return false;
	}

	filterByOutdated(row): boolean {
		if (this.outdated === false) {
			return true;
		} else {
			if (row.translation !== null) {
				if (this.isTranslationOutdated(row)) {
					return true;
				}
			}
		}
		return false;
	}

	filterMessages() {
		this.dataSource.filter = '{';
	}


	async invalidateTranslation(message: any) {
		this.invalidateTranslationEvent.emit(message);
	}

	isTranslationOutdated(message: MessageForTranslator): boolean {
		if (message.translation !== null) {
			return message.updateDate > message.translation.updateDate;
		} else {
			return false;
		}
	}

}
