#include <SPI.h>
#include <String.h>
#include <Ethernet.h>
//#include <LiquidCrystal.h>
#include <Ultrasonic.h>
#include <TimeLib.h>

#define pino_trigger 5
#define pino_echo 6
#define TempAlimen 1000
#define intevalo 2000 //intervalo entre girar motor
#define TamRes 20 //Tamanho reservatorio

/*@autor Vitor Hugo França de Paula
 * Matheus Henrique Alves
* Código para alimentar o pet 
*/
int Int = 2;
int Int1 = 3;


int time_now = now();
unsigned long previousMillis = 0;

Ultrasonic ultrasonic(pino_trigger, pino_echo);
//LiquidCrystal lcd(22,23,3,6,7,8,9,10,11,12,13);




byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
 //Usar servidor(IP) do coltec!!!!
IPAddress ip(10 ,0 ,0 , 20);
EthernetServer server(80);   

byte servidor[] = { 10, 0, 0, 24 };

//String readString = String(30);
unsigned long previousMillis = 0;
const long interval = 20000; //Intervalo de 20 segundos

//=================================================================
//               Area de declaração dos sensores
float Motor = 0;
//=================================================================

char serveri[] = "http://localhost/petfeedr";
EthernetClient client;

void setup() {
  Ethernet.begin(mac, ip);
  Serial.begin(9600);
  pinMode(Int,OUTPUT);
  pinMode(Int1,OUTPUT);
  digitalWrite(Int,LOW);
  digitalWrite(Int1,LOW);
}
/*
void inicio(){
  delay(1000);
  lcd.setCursor(4,0);
  lcd.print("PET FEEDER");
  delay(1000);
  lcd.setCursor(3,1);
  lcd.print("Bem vindo");
  delay(1000);
  lcd.print(".");
  delay(1000);
  lcd.print(".");
  delay(1000);
  lcd.print(".");
  delay(1000);
  lcd.clear();

}
void food(){
  delay(1000);
  lcd.print("Alimentando ");
   delay(1000);
  lcd.print("s2");
  delay(1000);
  lcd.print("s2");
  delay(1000);
  lcd.setCursor(0,1);
  lcd.clear();
}
*/
int Verifica(){
  const long interval = intevalo; //Intervalo de tempo entre cada vez que alimenta o pet
  unsigned long currentMillis = millis();
  
   if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      currentMillis = 0;
      return 1;
   }
   
   return 0;
  
}
int nivel(){
  float cmMsec;
  long microsec = ultrasonic.timing();
  cmMsec = ultrasonic.convert(microsec, Ultrasonic::CM);
  return cmMsec;
  
}



//===================================================================
//                             LOOP
//===================================================================



void loop() {
  
  EthernetClient cliente = server.available();
  unsigned long currentMillis = millis();

  int Aux,niv, por;
  Aux = Verifica();
  niv = nivel();
  por = (niv* 100) /TamRes;

  if(niv >= TamRes){
    Serial.print("Porcentagem de Carga :");//colocar no site tambem
    Serial.print(por);
    Serial.println("%");
    delay(1000);

    if(Aux == 1){
      
      digitalWrite(Int,HIGH);
      digitalWrite(Int1,LOW);
      delay(TempAlimen);//Tempo de Alimetação
      if (client.connect(servidor, 8095)) { //Connecting at the IP address and port we saved before
    
        Serial.println("connected");
        client.println("GET /petfeedr/salvarDados.php?motor=1"); //Connecting and Sending values to database
        client.stop(); //Closing the connection
        }
      else {
        // if you didn't get a connection to the server:
        Serial.println("connection failed");
      }
    } else {
      digitalWrite(Int,LOW);    
      digitalWrite(Int1, LOW);      
    }
  }
  else if (niv == TamRes){
    // avisar no site
    Serial.println("Sem Comida");
    delay(1000);
  }

 
  if (cliente) {
    boolean currentLineIsBlank = true;
    
    while(cliente.connected()) {
     
      if (cliente.available()) {
      
        char c = cliente.read();
       
        if (c == '\n' && currentLineIsBlank) {
          cliente.println("HTTP/1.1 200 OK");
          cliente.println("Content-Type: text/html");
          cliente.println("Connection: close");
          cliente.println();
          
          cliente.println("<!DOCTYPE html>");
          
          cliente.println("<html>");
          
            cliente.println("<head>");
            
              cliente.println("<title>PetFeedr</title>");
              cliente.println("<meta name=\"viewport\" content=\"width=320\">");
              cliente.println("<meta name=\"viewport\" content=\"width=device-width\">");
              cliente.println("<meta http-equiv=\"Content-Type\" content=\"text/html\"; charset=utf-8\">");
              cliente.println("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">");
              cliente.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
              cliente.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://localhost:8095/linkSite/style.css\">");
             
            cliente.println("</head>");
          
            cliente.println("<body>");

              cliente.println("<img src=\"http://localhost:8095/petfeedr/logo.png\" class=\"img\">");
              cliente.println("<div class=\"wrapper-content\">");
                cliente.println("<div class=\"content\">");
                  cliente.println("<a href=\"http://localhost:8095/petfeedr/index.php\" class=\"paragraph\">Ir para o site</a>");
                cliente.println("</div>");
              cliente.println("</div>");
               
              
            cliente.println("</body>");
            
          cliente.println("</html>");

         break;
        }
         if (c == '\n') 
                {
                    currentLineIsBlank = true;
                    Serial.println("nao deu!");
                } 
                else if (c != '\r') 
                {
                    currentLineIsBlank = false;
                     Serial.println("nao deu1222!");
                }
        
      }
    }
  }
  delay(10000);
  
}
