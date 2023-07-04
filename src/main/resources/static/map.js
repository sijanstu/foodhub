
let mapOptions = {
    center:[27.7, 85.3],
    zoom: 17
}

let map = new L.map('map' , mapOptions);
//show everything bigger
map.setMaxZoom(18);
let layer = new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
map.addLayer(layer);


let marker = null;
map.on('click', (event)=> {

    if(marker !== null){
        map.removeLayer(marker);
    }

    marker = L.marker([event.latlng.lat , event.latlng.lng]).addTo(map);
    //increase the marker size
    marker.setZIndexOffset(1000);
    marker.bindPopup("<button onclick='search()' class='btn btn-primary btn-sm btn-block mb-2 mt-2 text-center font-weight-bold'    >Search for restaurants Here</button>").openPopup();
    document.getElementById('latitude').value = event.latlng.lat;
    document.getElementById('longitude').value = event.latlng.lng;
    
})

function search(){
    //got to another page /getRestaurants/{latitude}/{longitude}
    const latitude = document.getElementById('latitude').value;
    const longitude = document.getElementById('longitude').value;
    // window.location.href = '/getRestaurants/'+latitude+'/'+longitude+'/'+localStorage.getItem('userToken');
    window.location.href = '/restaurants?latitude='+latitude+'&longitude='+longitude;
}
