import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../../components/confirmation-dialog/confirmation-dialog.component';

@Injectable({
	providedIn: 'root'
})
export class ConfirmationDialogService {

	constructor(public dialog: MatDialog) {
	}

	openDialog(customMessage?: any) {
		let dialogRef: any;
		if (customMessage) {
			dialogRef = this.dialog.open(ConfirmationDialogComponent, {
				width: '250px',
				data: { customMessage: customMessage, buttons: ['Yes', 'No'] }
			});
		} else {
			dialogRef = this.dialog.open(ConfirmationDialogComponent, {
				width: '250px'
			});
		}
		return dialogRef.afterClosed();
	}
}
