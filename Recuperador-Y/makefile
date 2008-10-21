JFLAGS = -g
JC = javac
ROOT = $(PW)
SRC = $(ROOT)src
CLASS = $(ROOT)bin
DFLAGS= -d $(CLASS)

#JRE= $(JAVA_HOME)/jre/lib/rt.jar
.SUFFIXES: .java .class -777
.java.class:
	$(JC) $(JFLAGS) -classpath $(SRC) $*.java $(DFLAGS)
	
CLASSES = \
	$(SRC)/AcumuladorDeBytesTransferidos.java\
	$(SRC)/Registro.java\
	$(SRC)/Utilidades.java\
	$(SRC)/ListaTermino.java\
	$(SRC)/RegistroIn.java\
	$(SRC)/RegistroOut.java\
	$(SRC)/RegistroUnitario.java\
	$(SRC)/Archivo.java\
	$(SRC)/ArchivoConsulta.java\
	$(SRC)/ArchivoPath.java\
	$(SRC)/ArchHTML2.java\
	$(SRC)/ArchivoSecuencialDocumentos.java\
	$(SRC)/ArchivoTerminos.java\
	$(SRC)/ArchivoTexto.java\
	$(SRC)/CodigoGamma.java\
	$(SRC)/Consulta.java\
	$(SRC)/Filtro.java\
	$(SRC)/FiltroFrase.java\
	$(SRC)/FiltroStemming.java\
	$(SRC)/FiltroWord.java\
	$(SRC)/InputReader.java\
	$(SRC)/Lector.java\
	$(SRC)/Menu.java\
	$(SRC)/Nodo.java\
	$(SRC)/NodoHoja.java\
	$(SRC)/NodoInterno.java\
	$(SRC)/OperacionesConListas.java\
	$(SRC)/OrdenadorDeTerminos.java\
	$(SRC)/Parser.java\
	$(SRC)/PrincipalMerge.java\
	$(SRC)/ProcesoFiltrado.java\
	$(SRC)/ArbolBP.java
	

default: classes

classes: $(CLASSES:.java=.class)


clean:
	$(RM) $(CLASS)/*.class

run:
	java  -cp $(CLASS) PrincipalMerge



