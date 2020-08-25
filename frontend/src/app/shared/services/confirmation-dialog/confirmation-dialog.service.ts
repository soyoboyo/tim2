import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../../components/confirmation-dialog/confirmation-dialog.component';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
	providedIn: 'root'
})
export class ConfirmationDialogService {

	constructor(public dialog: MatDialog,
				private translateService: TranslateService) {
	}

	openDialog(customMessage?: any) {
		let dialogRef: any;
		if (customMessage) {
			dialogRef = this.dialog.open(ConfirmationDialogComponent, {
				width: '250px',
				data: {
					customMessage: customMessage,
					buttons: [this.translateService.instant('yes'), this.translateService.instant('no')]
				}
			});
		} else {
			dialogRef = this.dialog.open(ConfirmationDialogComponent, {
				width: '250px'
			});
		}
		return dialogRef.afterClosed();
	}
}
