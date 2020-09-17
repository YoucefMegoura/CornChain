#include <SoftwareSerial.h>

// We changed the temperature(by Potentiometer) 
// and the humidity(by Luxmeter) sensors only for the demonstration purposes

unsigned long lastConnectionTime = 0;
const unsigned long postingInterval = 1000;

//Blutooth Config
const int txPin = 2; 
const int rxPin = 3; 
SoftwareSerial BTserial(rxPin, txPin); // RX | TX

//Temperature
const int tempPin = A0;
const int tempLedPin = 10;
int tempValue = 0;


//Humidity 
const int humPin = A1;
const int humLedPin = 9;
int humValue = 0;


void setup() {
  Serial.begin(9600);
  BTserial.begin(9600); 

  pinMode(tempPin, INPUT);
  pinMode(tempLedPin, OUTPUT);

  pinMode(humPin, INPUT);
  pinMode(humLedPin, OUTPUT);

}

void loop() {

  delay(100);
  makeMeasurement();
  if (millis() - lastConnectionTime > postingInterval)
  {
    sendData();
  }

}

void makeMeasurement() {

  //Get values from sensors on the Analog PINs
  tempValue = analogRead(tempPin);
  humValue = analogRead(humPin);

  //Write in LEDs
  analogWrite(tempLedPin, ledAdapterValues(tempValue));
  analogWrite(humLedPin, ledAdapterValues(humValue));
  
}

void sendData() {
  // Create the 'json' string to send data.
  String json;
  
  json = "{temp:";
  json += String(temperatureAdapterValues(tempValue));
  json += ", hum:";
  json += String(humidityAdapterValues(humValue));
  json += "}";
    
  // send the data to the serial port
  Serial.println(json);
  BTserial.println(json);
  //message to the receiving device
  delay(20);
  
  // note the time that the connection was made
  lastConnectionTime = millis();

}
//convert 0 - 1023 input signal to 0 - 255 signal for LEDs
int ledAdapterValues(int input) {
    return map(input, 0, 1023, 0, 255);
}

//convert 0 - 1023 input signal to 0% - 100% for Humidity
int humidityAdapterValues(int input) {
    return map(input, 0, 1023, 0, 100);
}

//convert 0 - 1023 input signal to -50C° - 100C° for Temperature
int temperatureAdapterValues(int input) {
    return map(input, 0, 1023, -50, 100);
}


