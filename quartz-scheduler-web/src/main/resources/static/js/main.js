$(document).ready(function() {
	$('[data-toggle="tooltip"]').tooltip()

	const orderStatusDoughnutChart = document.getElementById('orderStatusDoughnutChart');

	if (orderStatusDoughnutChart) {
		$.ajax('/orders/stats', {
			method: 'GET',
			success: function(data) {

				const opaqueColors = [];
				const transparentColors = [];
				var i = 0;
				for (var i = 0; i < data.colors.length; i++) {
					const color = data.colors[i];
					opaqueColors[i] = `rgba(${color.red}, ${color.green}, ${color.blue}, 1)`;
					transparentColors[i] = `rgba(${color.red}, ${color.green}, ${color.blue}, 0.2)`;
				}

				const myChart = new Chart(orderStatusDoughnutChart, {
					type: 'doughnut',
					data: {
						labels: data.status,
						datasets: [{
							label: 'Orders',
							data: data.quantities,
							backgroundColor: transparentColors,
							borderColor: opaqueColors,
							borderWidth: 1
						}]
					}
				});
			}
		});
	}

});
