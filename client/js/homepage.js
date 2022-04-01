
function loadData() {
    getData('Europe');
    getData('Asia');
    getData('Oceania');
    getData('North America');
    getData('South America');
    getData('Africa');
}

function getData(continent) {
    let response = fetch('http://localhost:8080/computeCorrelation?continent='+continent, {cache: "force-cache"})
    //let response = fetch('http://localhost:8080/computeCorrelation?continent='+continent)
    .then(function(response) {
        return response.json();
    }).then(function(data) {
        document.getElementById(continent).value=data;
    });    
}


