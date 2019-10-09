import { AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { RestService } from '../../../shared/services/rest/rest.service';

import { Chart } from 'chart.js';


@Component({
	selector: 'app-aggregate-info',
	templateUrl: './aggregate-info.component.html',
	styleUrls: ['./aggregate-info.component.scss']
})
export class AggregateInfoComponent implements OnInit, OnChanges, AfterViewInit {

	@Input() selectedProjectId: number;
	aggregatedInfoList: any;
	aggregatedInfo: any = null;

	summaryChartElement;
	summaryChart;
	summaryChartId = 'summaryChart';
	labels: any = [];
	dataCorrect: any = [];
	dataIncorrect: any = [];

	constructor(private http: RestService) {
	}

	ngOnInit() {
		this.labels = ['label', 'srabel'];
		this.dataCorrect = [10, 20];

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

	ngAfterViewInit(): void {
		this.summaryChartElement = document.getElementById(this.summaryChartId);
		this.generateChart();
	}

	generateChart() {
		console.log('generage chart');
		this.summaryChart = new Chart(this.summaryChartElement, {
			type: 'bar',
			data: {
				labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
				datasets: [{
					label: this.labels,
					data: this.dataCorrect,
					backgroundColor: [
						'rgba(255, 99, 132, 0.2)',
						'rgba(54, 162, 235, 0.2)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(75, 192, 192, 0.2)',
						'rgba(153, 102, 255, 0.2)',
						'rgba(255, 159, 64, 0.2)'
					],
					borderColor: [
						'rgba(255, 99, 132, 1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					borderWidth: 1
				}]
			},
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						}
					}]
				}
			}
		});
		this.showElement(this.summaryChartElement);
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



	// generateChart() {
	// 	this.summaryChart = new Chart(this.summaryChartElement, {
	// 		type: 'bar',
	// 		data: {
	// 			labels: this.labels,
	// 			datasets: [{
	// 				label: 'Data',
	// 				data: this.histogramValues,
	// 				backgroundColor: 'rgba(0, 0, 160, 1)',
	// 				borderColor: 'rgba(0, 0, 160, 1)',
	// 				borderWidth: 1,
	// 				fill: false,
	// 				pointRadius: 1,
	// 				pointBorderWidth: 1
	// 			}]
	// 		},
	// 		options: {
	// 			scales: {
	// 				yAxes: [{
	// 					ticks: {
	// 						beginAtZero: true
	// 					}
	// 				}]
	// 			},
	// 			spanGaps: false,
	// 			elements: {
	// 				line: {
	// 					skipNull: true,
	// 					drawNull: false,
	// 				}
	// 			}
	// 		}
	// 	});
	// 	this.showElement(this.summaryChartElement);
	// }
}
