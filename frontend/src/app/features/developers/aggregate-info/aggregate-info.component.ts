import { AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { RestService } from '../../../shared/services/rest/rest.service';

import { Chart } from 'chart.js';
import { AggregatedLocale } from './types/AggregatedLocale';
import { UtilsService } from '../../../shared/services/utils-service/utils.service';


@Component({
	selector: 'app-aggregate-info',
	templateUrl: './aggregate-info.component.html',
	styleUrls: ['./aggregate-info.component.scss']
})
export class AggregateInfoComponent implements OnInit, OnChanges, AfterViewInit {

	@Input() selectedProjectId: number;
	aggregatedInfo: any = null;

	summaryChartElement;
	summaryChart;
	summaryChartId = 'summaryChart';
	private labels: string[] = [];
	private dataCorrect: number[] = [];
	private dataIncorrect: number[] = [];
	private dataMissing: number[] = [];
	private messagesTotal = 0;

	constructor(private http: RestService, private utilsService: UtilsService) {
	}

	ngOnInit() {
	}

	ngOnChanges(changes: SimpleChanges): void {
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
			this.messagesTotal = response.messagesTotal;
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
		this.summaryChart.options.scales.yAxes[0].ticks.max = this.messagesTotal;
		this.summaryChart.update();
	}

	generateChart() {
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
							beginAtZero: true,
							min: 0,
							max: this.messagesTotal
						}
					}]
				}
			}
		});
		this.utilsService.showElement(this.summaryChartElement);
	}

}
