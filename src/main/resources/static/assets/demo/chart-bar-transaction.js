var ctx = document.getElementById('transaction');
	  
	var config = {
		type: 'bar',
		data: {
			labels: [ // Date Objects
				'1월',
				'2월',
				'3월',
				'4월',
				'5월',
				'6월',
				'7월',
				'8월',
				'9월',
				'10월',
				'11월',
				'12월'
			],
			datasets: [{
				label: '낙찰된 경매 물품 갯수',
				backgroundColor: 'yellow',
				borderColor: 'yellow',
				fill: false,
				data: [
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50)
				],
			}, {
				label: '유찰된 진행중인 물품 갯수',
				backgroundColor: 'red',
				borderColor: 'red',
				fill: false,
				data: [
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50),
					Math.floor(Math.random() * 50)
				],
			} , 
			
			
			]
		},	
			
		options: {
			maintainAspectRatio: false,
			title: {
				text: 'Chart.js Time Scale'
			},
			scales: {
				yAxes: [{
					scaleLabel: {
						display: true,
						labelString: ''
					}
				}]
			},
		}
	};
	 
	//차트 그리기
	var myChart = new Chart(ctx, config);
	  
