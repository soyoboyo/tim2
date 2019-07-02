import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class UtilsService {

    constructor() {
    }

    public sortByProperty<T>(array: T[], propName: keyof T, order: 'ASC' | 'DESC'): void {
        array.sort((a, b) => {
            if (a[propName] < b[propName]) {
                return -1;
            }

            if (a[propName] > b[propName]) {
                return 1;
            }
            return 0;
        });

        if (order === 'DESC') {
            array.reverse();
        }
    }
}
