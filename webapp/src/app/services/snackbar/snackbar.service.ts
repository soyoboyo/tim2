import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Injectable({
    providedIn: 'root'
})
export class SnackbarService {

    constructor(private snackBar: MatSnackBar) {
    }

    snackSuccess(message: string, action: string) {
        this.snackBar.open(message, action, { panelClass: ['snackbar-success'] });
    }

    snackError(message: string, action: string) {
        this.snackBar.open(message, action, {
            duration: 15000,
            panelClass: ['snackbar-error']
        });
    }
}
