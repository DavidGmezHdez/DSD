var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");

var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

var httpServer = http.createServer(
	function(request, response) {
		var uri = url.parse(request.url).pathname;
		if (uri=="/") uri = "/cliente.html";
		var fname = path.join(process.cwd(), uri);
		fs.exists(fname, function(exists) {
			if (exists) {
				fs.readFile(fname, function(err, data){
					if (!err) {
						var extension = path.extname(fname).split(".")[1];
						var mimeType = mimeTypes[extension];
						response.writeHead(200, mimeType);
						response.write(data);
						response.end();
					}
					else {
						response.writeHead(200, {"Content-Type": "text/plain"});
						response.write('Error de lectura en el fichero: '+uri);
						response.end();
					}
				});
			}
			else{
				console.log("Peticion invalida: "+uri);
				response.writeHead(200, {"Content-Type": "text/plain"});
				response.write('404 Not Found\n');
				response.end();
			}
		});
	}
);

var estadoAire = "Off";
var estadoPersiana = "Bajada";
var estadoPlay ="Apagada";
var estadoTV ="Apagada";

var luz,temp,play,tv;

var maxTemp = 30;
var minTemp = 15;

var maxLuz = 80;
var minLuz = 10;



MongoClient.connect("mongodb://localhost:27017/", function(err, db) {
	httpServer.listen(8080, function(){
		console.log("Servidor inciado");
	});
	var io = socketio.listen(httpServer);

	var dbo = db.db("DomoticoBD");
	dbo.createCollection("domotico", function(err, collection){
    	io.sockets.on('connection',
		function(client) {
			//Funcion del agente | Hace de controlador
			client.on("agente",function(datos){
				collection.insert(datos, {safe:true}, function(err, result) {});
				
				luz = datos.luz;
				temp = datos.temp;
				play = datos.play;
				tv = datos.tv;

				if(luz > maxLuz)
					estadoPersiana = "Bajada";
				else if (luz < minLuz)
					estadoPersiana = "Subida";

				if(temp < minTemp || temp > maxTemp)
					estadoAire = "On";
				else
					estadoAire = "Off";

				if(play == "Si")
					estadoPlay = "Encendida";
				else if(play=="No")
					estadoPlay = "Apagada";

				if(tv == "Si")
					estadoTV = "Encendida";
				else if(tv=="No")
					estadoTV = "Apagada";

				io.sockets.emit("actualizarAire", {estadoAire:estadoAire,temp:temp});
				io.sockets.emit("actualizarPersiana", {estadoPersiana:estadoPersiana,luz:luz});
				io.sockets.emit("actualizarPlay", estadoPlay);
				io.sockets.emit("actualizarTV", estadoTV);
			});


			//Cambiar estados
			client.on('cambiarEstadoAire',function(){
				if(estadoAire == "Off")
					estadoAire = "On";
				else
					estadoAire = "Off";
				io.sockets.emit('actualizarAire',{estadoAire:estadoAire,temp:temp});
			});

			client.on('cambiarEstadoPersiana',function(){
				if(estadoPersiana == "Bajada")
					estadoPersiana = "Subida";
				else
					estadoPersiana = "Bajada";
				io.sockets.emit('actualizarPersiana',{estadoPersiana:estadoPersiana,luz:luz});
			});

			client.on('cambiarEstadoPlay',function(){
				if(estadoPlay == "Apagada")
					estadoPlay = "Encendida";
				else
					estadoPlay = "Apagada";
				io.sockets.emit('actualizarPlay',estadoPlay);
			});

			client.on('cambiarEstadoTV',function(){
				if(estadoTV == "Apagada")
					estadoTV = "Encendida";
				else
					estadoTV = "Apagada";
				io.sockets.emit('actualizarTV',estadoTV);
			});

			//Alertas

			client.on("actualizarAlertas",function(){
				var alertaLuz;
				if(estadoPersiana == "Bajada")
					alertaLuz = "Se ha bajado la persiana";
				else if (estadoPersiana == "Subida")
					alertaLuz = "Se ha subido la persiana";
				
				var alertaAire;
				if(estadoAire == "On")
					alertaAire = "Se ha encendido el aire";
				else if (estadoAire == "Off")
					alertaAire = "Se ha apagado el aire";
						
				var alertaSimultaneos="";
				if(estadoAire=="On" && estadoPersiana =="Subida")
					alertaSimultaneos="¡CUIDADO! Tienes la persana subida y el aire puesto";
				else
					alertaSimultaneos="";
				
				var alertaPlay;
				if(estadoPlay == "Encendida")
					alertaPlay = "Se ha encendido la Play";
				else if(estadoPlay == "Apagada")
					alertaPlay = "Se ha apagado la Play";

				var alertaTV;
				if(estadoTV == "Encendida")
					alertaTV = "Se ha encendido la TV";
				else if(estadoTV == "Apagada")
					alertaTV = "Se ha apagado la TV";

				var alertaTVPlay ="";
				if(estadoPlay == "Encendida" && estadoTV == "Apagada")
					var alertaTVPlay ="¡CUIDADO! Tienes la Play encendida pero la TV apagada. Normal que no veas nada";
				else if(estadoPlay == "Apagada" && estadoTV == "Encendida")
					var alertaTVPlay ="¡CUIDADO! Tienes la TV encendida pero la Play apagada. Así no puedes viciar";
				else
					alertaTVPlay="";

				io.sockets.emit("alertaSimultaneos", alertaSimultaneos);
				io.sockets.emit("alertaAire", alertaAire);
				io.sockets.emit("alertaLuz", alertaLuz);
				io.sockets.emit("alertaPlay", alertaPlay);
				io.sockets.emit("alertaTV", alertaTV);
				io.sockets.emit("alertaTVPlay", alertaTVPlay);
			});
			
		});
    });
});

console.log("Servicio MongoDB iniciado");

