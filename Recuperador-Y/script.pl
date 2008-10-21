#!/bin/perl

#=========================================
use Getopt::Std;
#Constantes para EOL
use constant EOL => "\n";
#=========================================


#**************************************************************
sub usage
{
	print "***********************************************************************************************".EOL;
	print "Organización de Datos - 7506 " . EOL;	
	print "Comando auxiliar para la obtención de documentos .html " . EOL;
	print "***********************************************************************************************".EOL;
	print "Parametros:" .EOL;
	print "\t-h \t\t\tImprime esta ayuda" . EOL;
	print "\t-p [path] \t\tIndica el path donde deben almacenarse los archivos (path absoluto)" . EOL;
	exit 0;
}
#***************************************************************
sub parametros_incorrectos
{
	print STDERR "Parametros Incorrectos" . EOL;
	exit 1;
}
#***************************************************************



#=========================================
#Variables
my %opciones;
my $result;
my $path;
my $archivo;

#Obtengo los parametros de la linea de comandos
$result = getopts('hp:l:', \%opciones);

#Chequeo parametros
if ((not $result) or (@ARGV > 0))
{
	parametros_incorrectos;
}

# Imprimo la ayuda
if  ($opciones{h})
{
	usage;
}

#Se debe definir el path para almacenamiento de los archivos
if (not $opciones{p})
{	
	parametros_incorrectos;
}

#Se debe indicar el archivo de links-doc
if (not $opciones{l})
{	
	parametros_incorrectos;
}

$path = $opciones{p};
$archivo = $opciones{l};

#Recorro el archivo obteniendo los documentos correspondientes
foreach  $linea (`cat $archivo`) {
	my ($url, $doc)=split(" ", $linea);

	#Si es un html lo bajo
	if(rindex($url, "html")>0){
		$doc = $path . $doc;
		`GET $url > $doc`;
	}
}

exit 0;

