var ctx = document.getElementById('customerjoin');
	  
	var config = {
		type: 'line',
		data: {
			labels: [ // Date Objects
				'1월',
				'2월',
				'4월',
				'6월',
				'8월',
				'10월',
				'12월',
				
			
			],
			datasets: [{
				label: '가입 현황',
				backgroundColor: 'rgba(90, 192, 192, 1)',
				borderColor: 'rgba(90, 192, 192, 1)',
				fill: false,
				data: [
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100)
				],
			}, {
				label: '탈퇴 현황',
				backgroundColor: 'rgba(130, 99, 132, 1)',
				borderColor: 'rgba(130, 99, 132, 1)',
				fill: false,
				data: [
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100),
					Math.floor(Math.random() * 100)
				],
			}]
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
	  
