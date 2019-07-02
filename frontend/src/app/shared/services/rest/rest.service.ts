import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/internal/operators';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { saveAs } from 'file-saver';
import { Project } from '../../types/entities/Project';

@Injectable({
	providedIn: 'root'
})
export class RestService {
	URL = environment.API + '/api/v1/';

	constructor(private http: HttpClient) {
	}

	async getAll(url: string) {
		return await this.http.get<any>(this.URL + url, { withCredentials: true })
		.pipe(
			catchError(this.handleError)
		).toPromise();
	}

	save(url: string, body: any): Observable<any> {
		return this.http.post<any>(this.URL + url, body, { withCredentials: true })
		.pipe(
			catchError(this.handleError)
		);
	}

	update(url: string, body: any): Observable<any> {
		return this.http.post<any>(this.URL + url, body, { withCredentials: true })
		.pipe(
			catchError(this.handleError)
		);
	}

	remove(url: string) {
		return this.http.delete<any>(this.URL + url, { withCredentials: true })
		.pipe(
			catchError(this.handleError)
		);
	}

	async downloadXLS(project: Project) {
		const url = this.URL + 'message/export/getByProject/' + project.id;
		const d = new Date();
		const now = d.getDate() + "-" + (d.getMonth() + 1) + "-" + d.getFullYear() + " " +
			d.getHours() + ":" + d.getMinutes();
		const filename = 'report_' + project.name + '_' + now + '.xlsx';

		await this.http.get(url, {
			responseType: 'blob',
			withCredentials: true
		}).subscribe(response => {
			let blob = new Blob([response], { type: 'application/xml' });
			saveAs(blob, filename);
		});

	}


	private handleError(error: HttpErrorResponse) {
		if (error.error instanceof ErrorEvent) {
			console.error('An error occurred:', error.error.message);
		} else {
			console.error(
				`Backend returned code ${error.status}, ` +
				`exception was: ${error.error.message}`);
		}
		return throwError(error);
	}
}
