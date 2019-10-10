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
	labels: string[] = [];
	dataCorrect: number[] = [];
	dataIncorrect: number[] = [];

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
			console.log(response);
			this.aggregatedInfoList = response.translationStatusesByLocale;
			this.parseData(response['translationStatusesByLocale']);
		}
	}

	private parseData(aggregatedInfo: any) {
		this.labels = [];
		this.dataCorrect = [];
		this.dataIncorrect = [];
		Object.keys(aggregatedInfo).forEach((key) => {
			this.labels.push(key);
			this.dataCorrect.push(aggregatedInfo[key].correct);
			const incorrect = aggregatedInfo[key].missing + aggregatedInfo[key].incorrect;
			this.dataIncorrect.push(incorrect);
		});
		this.generateChart();
		console.log(this.labels);
		console.log(this.dataCorrect);
		console.log(this.dataIncorrect);

		this.generateChart();
	}

	ngAfterViewInit(): void {
		this.summaryChartElement = document.getElementById(this.summaryChartId);

	}

	generateChart() {
		console.log('generage chart');
		this.summaryChart = new Chart(this.summaryChartElement, {
			type: 'bar',
			data: {
				labels: this.labels,
				datasets: [{
					label: 'Correct',
					data: this.dataCorrect,
					backgroundColor: 'rgba(0, 255, 0, 1)'
				}, {
					label: 'Incorrect',
					data: this.dataIncorrect,
					backgroundColor: 'rgba(255, 0, 0, 1)'
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
