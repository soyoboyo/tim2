import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface DialogData {
    customMessage: string;
    buttons: string[];
}

@Component({
    selector: 'app-confirmation-dialog',
    templateUrl: './confirmation-dialog.component.html',
    styleUrls: ['./confirmation-dialog.component.css']
})
export class ConfirmationDialogComponent implements OnInit {

    constructor(public dialogRef: MatDialogRef<ConfirmationDialogComponent>,
                @Inject(MAT_DIALOG_DATA) public data: DialogData) {
    }

    ngOnInit() {
    }

    onNoClick(): void {
        this.dialogRef.close(false);
    }

    toRemove() {
        this.dialogRef.close(true);
    }
}
