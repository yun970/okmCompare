google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawChart);
let arr = [];
let price = [];

let link1 = `http://localhost:8080`;
let link2 = `http://15.164.249.252`;
let todayPrice;
const url = new URL(window.location.href);


 function drawChart() {

    const url = new URL(window.location.href);
    let num = url.searchParams.get('num');
    fetch(`${link1}/datalist/${num}`)
      .then(response => response.json())
      .then(data => {
        data.sort((a, b) => {
          const dateA = new Date(a.createDate);
          const dateB = new Date(b.createDate);
          return dateA - dateB;
        });

        console.log(data);
        for (let i in data) {
          let date = new Date(data[i].createDate);
          let b = `${date.getUTCFullYear()}-${date.getUTCMonth()+1}-${date.getUTCDate()}`;
          console.log(b);
          arr.push(b);
          price.push(data[i].productPrice);
          todayPrice = data[i]
        }

        const dataTable = new google.visualization.DataTable();
        dataTable.addColumn('string', '날짜');
        dataTable.addColumn('number', '가격');
        for (let i = 0; i < arr.length; i++) {
          console.log(arr[i])
          let b = arr[i].substring(5);
          dataTable.addRow([b, price[i]]);
        }
        console.log(dataTable);
        const options = {
          title: 'Price',
          curveType: 'function',
          legend: { position: 'none' },
          vAxis: {
            format: "#,###",
          },
          hAxis: {
            gridlines:{
                count:10,
            },
            ticks: [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180]
        }};

        const chart = new google.visualization.LineChart(document.getElementById('Chart'));
        chart.draw(dataTable, options);
      });
  }
  function filterData(period) {
    const currentDate = new Date(); // 현재 날짜
    const filteredData = [];
    
    // 선택한 기간에 따라 필터링된 데이터 생성
    if (period === '1w') {
      const buttonTime = new Date(currentDate);
      buttonTime.setDate(buttonTime.getDate() - 7);
      
      for (let i = 0; i < arr.length; i++) {
        const dataDate = new Date(arr[i]);
        if (dataDate >= buttonTime) {
          let b = arr[i].substring(5);
          filteredData.push([b, price[i]]);
        }
      }
    } else if (period === '3m') {
      const buttonTime = new Date(currentDate);
      buttonTime.setDate(buttonTime.getDate() - 90);
      
      for (let i = 0; i < arr.length; i++) {
        const dataDate = new Date(arr[i]);
        if (dataDate >= buttonTime) {
          let b = arr[i].substring(5);
          filteredData.push([b, price[i]]);
        }
      }
      // 3개월 로직 추가
    } else if (period === '1y') {
      const buttonTime = new Date(currentDate);
      buttonTime.setDate(buttonTime.getDate() - 365);
      
      for (let i = 0; i < arr.length; i++) {
        const dataDate = new Date(arr[i]);
        if (dataDate >= buttonTime) {
          let b = arr[i].substring(5);
          filteredData.push([b, price[i]]);
        }
      }
    }
    
    // 필터링된 데이터로 차트 다시 그리기
    const dataTable = new google.visualization.DataTable();
    dataTable.addColumn('string', '날짜');
    dataTable.addColumn('number', '가격');
    for (let i = 0; i < filteredData.length; i++) {
      dataTable.addRow(filteredData[i]);
    }
    
    const options = {
      title: 'Price',
      curveType: 'function',
      legend: { position: 'none' },
      vAxis: {},
    };
    
    const chart = new google.visualization.LineChart(document.getElementById('Chart'));
    chart.draw(dataTable, options);
  }
  
function alarm(){
    console.log("asdf");
    let num = url.searchParams.get('num');
    let price = todayPrice;
    email = 'scc00057@naver.com';


    fetch(`${link1}/api/v1`,
    {
    method: "POST",
    headers:{
        "Content-Type": "application/json",
    },
    body: JSON.stringify({
            email: email,
            productNum: Number(num),
            price: price,
        }),
    })
    .then((response) => console.log(response));

};
