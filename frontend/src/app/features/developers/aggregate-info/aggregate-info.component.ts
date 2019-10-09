import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { RestService } from '../../../shared/services/rest/rest.service';


declare var Chart: any;

@Component({
	selector: 'app-aggregate-info',
	templateUrl: './aggregate-info.component.html',
	styleUrls: ['./aggregate-info.component.scss']
})
export class AggregateInfoComponent implements OnInit, OnChanges {

	@Input() selectedProjectId: number;
	aggregatedInfoList: any;
	aggregatedInfo = null;

	histogramChartElement;
	histogramChart;

	histogramData: any = [];
	histogramIndexes: any = [];
	histogramValues: any = [];
	histogramChartId = 'histogramChart';
	labels: any = [];
	constructor(private http: RestService) {
	}

	ngOnInit() {
	}

	ngOnChanges(changes: SimpleChanges): void {
		this.getAggregatedInfo();
	}

	public async getAggregatedInfo() {
		if (this.selectedProjectId !== null) {
			const response = await this.http.getAll('project/developer/aggregate/' + this.selectedProjectId);
			this.aggregatedInfo = response;
			this.aggregatedInfoList = response.translationStatusesByLocale;
		}
	}

	generateChart() {
		this.histogramChart = new Chart(this.histogramChartElement, {
			type: 'bar',
			data: {
				labels: this.labels,
				datasets: [{
					label: 'Data',
					data: this.histogramValues,
					backgroundColor: 'rgba(0, 0, 160, 1)',
					borderColor: 'rgba(0, 0, 160, 1)',
					borderWidth: 1,
					fill: false,
					pointRadius: 1,
					pointBorderWidth: 1
				}]
			},
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						}
					}]
				},
				spanGaps: false,
				elements: {
					line: {
						skipNull: true,
						drawNull: false,
					}
				}
			}
		});
		this.showElement(this.histogramChartElement);
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
