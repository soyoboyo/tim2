import { AfterViewInit, Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { RestService } from '../../../shared/services/rest/rest.service';

import { Chart } from 'chart.js';
import { AggregatedLocale } from './AggregatedLocale';
import { UtilsService } from '../../../shared/services/utils-service/utils.service';


@Component({
	selector: 'app-aggregate-info',
	templateUrl: './aggregate-info.component.html',
	styleUrls: ['./aggregate-info.component.scss']
})
export class AggregateInfoComponent implements OnInit, OnChanges, AfterViewInit, OnDestroy {

	@Input() selectedProjectId: number;
	aggregatedInfo: any = null;

	summaryChartElement;
	summaryChart;
	summaryChartId = 'summaryChart';
	private labels: string[] = [];
	private dataCorrect: number[] = [];
	private dataIncorrect: number[] = [];
	private dataMissing: number[] = [];

	constructor(private http: RestService, private utilsService: UtilsService) {
	}

	ngOnInit() {
	}

	ngOnDestroy(): void {
		console.log('on destory');
	}

	ngOnChanges(changes: SimpleChanges): void {
		console.log(changes);
		this.getAggregatedInfo();
	}

	ngAfterViewInit(): void {
		this.summaryChartElement = document.getElementById(this.summaryChartId);
		this.generateChart();
	}

	public async getAggregatedInfo() {
		if (this.selectedProjectId !== null) {
			const response = await this.http.getAll('project/developer/aggregate/' + this.selectedProjectId);
			this.aggregatedInfo = response;
			console.log(response);
			this.parseData(response.aggregatedLocales);
		}
	}

	private parseData(aggregatedInfo: AggregatedLocale[]) {
		this.labels = [];
		this.dataCorrect = [];
		this.dataIncorrect = [];
		this.dataMissing = [];
		aggregatedInfo.forEach((aggregatedLocale) => {
			this.labels.push(aggregatedLocale.locale);
			this.dataCorrect.push(aggregatedLocale.correct);
			this.dataIncorrect.push(aggregatedLocale.incorrect);
			this.dataMissing.push(aggregatedLocale.missing);
		});
		this.updateChart();
	}

	private updateChart() {
		console.log('update chart');
		this.summaryChart.data.labels = this.labels;
		this.summaryChart.data.datasets = [
			{
				label: 'Correct',
				data: this.dataCorrect,
				backgroundColor: 'rgba(30, 204, 79, 1)'
			}, {
				label: 'Incorrect',
				data: this.dataIncorrect,
				backgroundColor: 'rgba(204, 207, 38, 1)'
			}, {
				label: 'Missing',
				data: this.dataMissing,
				backgroundColor: 'rgba(204, 41, 30, 1)'
			}];
		this.summaryChart.update();
	}

	generateChart() {
		console.log('generate chart');
		this.summaryChart = new Chart(this.summaryChartElement, {
			type: 'bar',
			data: {
				labels: this.labels,
				datasets: [
					{
						label: 'Correct',
						data: this.dataCorrect,
						backgroundColor: 'rgba(30, 204, 79, 1)'
					}, {
						label: 'Incorrect',
						data: this.dataIncorrect,
						backgroundColor: 'rgba(204, 207, 38, 1)'
					}, {
						label: 'Missing',
						data: this.dataMissing,
						backgroundColor: 'rgba(204, 41, 30, 1)'
					}]
			},
			options: {
				scales: {
					xAxes: [{
						stacked: true
					}],
					yAxes: [{
						stacked: true,
						ticks: {
							beginAtZero: true
						}
					}]
				}
			}
		});
		this.utilsService.showElement(this.summaryChartElement);
	}

}
