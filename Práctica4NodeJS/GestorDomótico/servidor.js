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
		if (uri=="/") uri = "/servidor.html";
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

var aire = "Off";
var persiana = "Bajada";
var videoconsola ="Apagada";
var television ="Apagada";

var luz,temp,play,tv;

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
				datos.conexion = 'New connection from ' + client.request.connection.remoteAddress + ':' + client.request.connection.remotePort;
				collection.insert(datos, {safe:true}, function(err, result) {});
				
				luz = datos.luz;
				temp = datos.temp;
				play = datos.play;
				tv = datos.tv;

				if(luz > 80)
					persiana = "Bajada";
				else if (luz < 10)
					persiana = "Subida";

				if(temp < 15)
					aire = "Encendido (calor)";
				else if(temp > 30)
					aire = "Encendido (frío)";
				else
				aire = "Apagado";

				if(play == "Si")
					videoconsola = "Encendida";
				else if(play =="No")
					videoconsola = "Apagada";

				if(tv == "Si")
					television = "Encendida";
				else if(tv=="No")
					television = "Apagada";

				io.sockets.emit("nuevoEstadoAire", {aire:aire,temp:temp});
				io.sockets.emit("nuevoEstadoPersiana", {persiana:persiana,luz:luz});
				io.sockets.emit("nuevoEstadoPlay", videoconsola);
				io.sockets.emit("nuevoEstadoTV", television);
			});


			//Cambiar estados
			client.on('cambiarAire',function(){
				if(aire == "Off"){
					if(temp < 15)
						aire = "Encendido (calor)";
					else if(temp > 30)
						aire = "Encendido (frío)";
				}
				else{	
					aire="Off";
				}

				io.sockets.emit('nuevoEstadoAire',{aire:aire,temp:temp});
			});

			client.on('cambiarPersiana',function(){
				if(persiana == "Bajada")
					persiana = "Subida";
				else
					persiana = "Bajada";
				io.sockets.emit('nuevoEstadoPersiana',{persiana:persiana,luz:luz});
			});

			client.on('cambiarPlay',function(){
				if(videoconsola == "Apagada")
					videoconsola = "Encendida";
				else
					videoconsola = "Apagada";
				io.sockets.emit('nuevoEstadoPlay',videoconsola);
			});

			client.on('cambiarTV',function(){
				if(estadoTV == "Apagada")
					television = "Encendida";
				else
					television = "Apagada";
				io.sockets.emit('nuevoEstadoTV',television);
			});

			//Alertas

			client.on("actualizarAlertas",function(){
				var alertaLuz;
				if(persiana == "Bajada")
					alertaLuz = "Se ha bajado la persiana";
				else if (persiana == "Subida")
					alertaLuz = "Se ha subido la persiana";
				
				var alertaAire;
				if(aire == "Encendido (calor)" || aire == "Encendido (frío)")
					alertaAire = "Se ha encendido el aire";
				else if (aire == "Off")
					alertaAire = "Se ha apagado el aire";
						
				var alertaSimultaneos="";
				if((aire=="Encendido (calor)" || aire=="Encendido (frío)") && persiana =="Subida")
					alertaSimultaneos="¡CUIDADO! Tienes la persana subida y el aire puesto";
				else
					alertaSimultaneos="";
				
				var alertaPlay;
				if(videoconsola == "Encendida")
					alertaPlay = "Se ha encendido la Play";
				else if(videoconsola == "Apagada")
					alertaPlay = "Se ha apagado la Play";

				var alertaTV;
				if(television == "Encendida")
					alertaTV = "Se ha encendido la TV";
				else if(television == "Apagada")
					alertaTV = "Se ha apagado la TV";

				var alertaTVPlay ="";
				if(videoconsola == "Encendida" && television == "Apagada")
					var alertaTVPlay ="¡CUIDADO! Tienes la Play encendida pero la TV apagada. Normal que no veas nada";
				else if(videoconsola == "Apagada" && television == "Encendida")
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

