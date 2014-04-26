#include <Wire.h>
#include <ADXL345.h>

ADXL345 adxl; //variable adxl is an instance of the ADXL345 library
String statusGuncangan = "Normal";
float koordinatLatitude,koordinatLongitude;
char arahLat, arahLon;
int stat=0;
int n=0;
int i=0;
int x,y,z;  
float xx, yy, zz;

//String datagps = "$GPGGA,031121.000,0658.185052,S,10738.116296,E,1,5,3.87,34.464,M,3.083,M,,*47";

void setup(){
  pinMode(3,OUTPUT);//The default digital driver pins for the GSM and GPS mode
  pinMode(4,OUTPUT);
  pinMode(5,OUTPUT);
  digitalWrite(5,HIGH);
  delay(1500);
  digitalWrite(5,LOW);

  digitalWrite(3,LOW);//Enable GSM mode
  digitalWrite(4,HIGH);//Disable GPS mode
  delay(2000);
  Serial.begin(9600); 
  delay(5000);//GPS ready

  adxl.powerOn();

  //set activity / inactivity thresholds (0-255)
  adxl.setActivityThreshold(75); //62.5mg per increment
  adxl.setInactivityThreshold(75); //62.5mg per increment
  adxl.setTimeInactivity(10); // how many seconds of no activity is inactive?

  //look of activity movement on this axes - 1 == on; 0 == off 
  adxl.setActivityX(1);
  adxl.setActivityY(1);
  adxl.setActivityZ(1);

  //look of inactivity movement on this axes - 1 == on; 0 == off
  adxl.setInactivityX(1);
  adxl.setInactivityY(1);
  adxl.setInactivityZ(1);

  //look of tap movement on this axes - 1 == on; 0 == off
  adxl.setTapDetectionOnX(0);
  adxl.setTapDetectionOnY(0);
  adxl.setTapDetectionOnZ(1);

  //set values for what is a tap, and what is a double tap (0-255)
  adxl.setTapThreshold(50); //62.5mg per increment
  adxl.setTapDuration(15); //625μs per increment
  adxl.setDoubleTapLatency(80); //1.25ms per increment
  adxl.setDoubleTapWindow(200); //1.25ms per increment

  //set values for what is considered freefall (0-255)
  adxl.setFreeFallThreshold(7); //(5 - 9) recommended - 62.5mg per increment
  adxl.setFreeFallDuration(45); //(20 - 70) recommended - 5ms per increment

  //setting all interupts to take place on int pin 1
  adxl.setInterruptMapping( ADXL345_INT_SINGLE_TAP_BIT,   ADXL345_INT1_PIN );
  adxl.setInterruptMapping( ADXL345_INT_DOUBLE_TAP_BIT,   ADXL345_INT1_PIN );
  adxl.setInterruptMapping( ADXL345_INT_FREE_FALL_BIT,    ADXL345_INT1_PIN );
  adxl.setInterruptMapping( ADXL345_INT_ACTIVITY_BIT,     ADXL345_INT1_PIN );
  adxl.setInterruptMapping( ADXL345_INT_INACTIVITY_BIT,   ADXL345_INT1_PIN );

  //register interupt actions - 1 == on; 0 == off  
  adxl.setInterrupt( ADXL345_INT_SINGLE_TAP_BIT, 1);
  adxl.setInterrupt( ADXL345_INT_DOUBLE_TAP_BIT, 1);
  adxl.setInterrupt( ADXL345_INT_FREE_FALL_BIT,  1);
  adxl.setInterrupt( ADXL345_INT_ACTIVITY_BIT,   1);
  adxl.setInterrupt( ADXL345_INT_INACTIVITY_BIT, 1);

}

void loop(){
  while(1){
    if(stat==0){
      initGPS();
      stat=1;
    }

    if(stat=1){
      latitude();
      longitude();
      if(koordinatLatitude!=0 && koordinatLongitude!=0){
        stat=2;
      }
    } 

    if(stat==2){
      for(n=0;n<=3;n++){
        Accelero();
        initGPRS();
        SettingGPRS();
        SendGPRS();
      }
      stat=3;
    }

    if(stat==3){
      waitMode();
    }

    if(n==3){
      stat=3;
    } 

//    Serial.println("===");
  }  
}

void Accelero(){
  //Boring accelerometer stuff   
  adxl.readAccel(&x, &y, &z); //read the accelerometer values and store them in variables  x,y,z
  xx = x * 9.8 * 0.004;
  yy = y * 9.8 * 0.004;
  zz = z * 9.8 * 0.004;

  byte interrupts = adxl.getInterruptSource();

  // freefall
  if(adxl.triggered(interrupts, ADXL345_FREE_FALL)){
    //add code here to do when freefall is sensed
    statusGuncangan = "Jatuh";
    stat=0;
  } 

  //inactivity
  if(adxl.triggered(interrupts, ADXL345_INACTIVITY)){
    //add code here to do when inactivity is sensed
    statusGuncangan = "Normal";
    //    Serial.println(statusGuncangan);
  }

  //activity
  if(adxl.triggered(interrupts, ADXL345_ACTIVITY)){
    //add code here to do when activity is sensed
    statusGuncangan = "Guncangan";
    stat=0;
    //    Serial.println(statusGuncangan);
  }
}

void SendGPRS(){
  Serial.print("AT+HTTPPARA=\"URL\",\"http://lacak-men.web.id/gpstracker/gps_tracker.php?id=12&lat=");
  Serial.print(koordinatLatitude,6);
  Serial.print("&long=");
  Serial.print(koordinatLongitude,6);
  Serial.print("&x=");
  Serial.print(xx);
  Serial.print("&y=");
  Serial.print(yy);
  Serial.print("&z=");
  Serial.print(zz);
  Serial.print("&status=");
  Serial.print(statusGuncangan);
  Serial.println("\"");
  delay(3000);
  Serial.println("AT+HTTPACTION=0");
  delay(5000);
  Serial.println("AT+HTTPREAD");
  delay(3000);
  Serial.println("AT+HTTPTERM");
  delay(1000);
}

void SettingGPRS (){
  Serial.println("AT");  
  delay(1000);
  //Send message
  Serial.println("AT+CGDCONT?");
  delay(1000);
  Serial.println("AT+CREG?");
  delay(1000);
  Serial.println("AT+CGACT?");
  delay(1000);
  Serial.println("AT+CMEE=1");
  delay(1000);
  Serial.println("AT+CGATT=1");
  delay(1000);
  Serial.println("AT+CGACT=1,1");
  delay(1000);
  Serial.println("AT+SAPBR=3,1,\"CONTYPE\",\"GPRS\"");
  delay(1000);
  Serial.println("AT+SAPBR=3,1,\"APN\",\"3data\"");
  delay(1000);
  Serial.println("AT+SAPBR=1,1");
  delay(1000);
  Serial.println("AT+SAPBR=2,1");
  delay(1000);
  Serial.println("AT+HTTPINIT");
  delay(1000); 
}

void initGPS(){
  Serial.println("AT");   
  delay(1000);
  //turn on GPS power supply
  Serial.println("AT+CGPSPWR=1");
  delay(1000);
  //reset GPS in autonomy mode
  Serial.println("AT+CGPSRST=1");
  delay(1000);
  digitalWrite(4,LOW);//Enable GPS mode
  digitalWrite(3,HIGH);//Disable GSM mode
  delay(1000);
}

void initGPRS (){
  digitalWrite(3,LOW);//Enable the GSM mode
  digitalWrite(4,HIGH);//Disable the GPS mode
  delay(2000);
}

//ambil id
char ID()//Match the ID commands
{ 
  char i=0;
  char value[6]={
    '$','G','P','G','G','A'  
  };//match the gps protocol
  char val[6]={
    '0','0','0','0','0','0'
  };

  while(1)
  {
    if(Serial.available())
    {
      val[i] = Serial.read();//get the data from the serial interface
      if(val[i]==value[i]) //Match the protocol
      {    
        i++;
        if(i==6)
        {
          i=0;
          return 1;//break out after get the command
        }
      }
      else
        i=0; 
    }
  } 
}
//=========== end of ambil id

//misahin koma
void comma(char num){   
  char val;
  char count=0;//count the number of ','
  while(1)
  {
    if(Serial.available())
    {
      val = Serial.read();
      if(val==',')
        count++;
    }
    if(count==num)//if the command is right, run return
      return;
  }
}
//========== end of misahin koma

//ARAH LAT
void lat_dir()//get dimensions
{
  char i=0,val;

  if(ID())
  {
    comma(3);
    while(1)
    {
      if(Serial.available())
      {
        val = Serial.read();
        arahLat = val;
        //        Serial.write(val);
        //        Serial.println();
        i++;
      }
      if(i==1)
      {
        i=0;
        return;
      }  
    }
  }
}
//===================

//ARAH LON
void lon_dir()//get direction data
{
  char i=0,val;

  if(ID())
  {
    comma(5);
    while(1)
    {
      if(Serial.available())
      {
        val = Serial.read();
        arahLon = val;
        //        Serial.write(val); //Serial.println(val,BYTE);
        //        Serial.println();
        i++;
      }
      if(i==1)
      {
        i=0;
        return;
      }  
    }
  }
}
//===============

//lat
void latitude()//get latitude
{  
  lat_dir();

  int i=0;
  float lati1, lati2, lati3, lati;
  char lat1[11]={
  };
  char lat2[11]={
  };

  if(ID())
  {
    comma(2);
    while(1)
    {
      if(Serial.available())
      {
        if(i<2){
          lat1[i] = Serial.read(); //0658.185052
        } 
        if(i>=2){
          lat2[i-2] = Serial.read();
        }
        i++;
      }
      if(i==10)
      {
        i=0;

        lati1 = atof(lat1);
        lati2 = atof(lat2);

        lati1 = lati1,6 - lati2,6;
        lati3 = lati2/60;

        lati = lati1 + lati3;

        if(arahLat == 'S'){
          lati = lati * -1;
        }
        koordinatLatitude = lati;
        //        Serial.print(lati,6);//print latitude 
        return;
      }  
    }
  }
}
//=============

//lon
void longitude()//get longitude
{
  lon_dir();

  int i=0;
  float longi1, longi2, longi3, longi;

  char lon1[12]={
  };
  char lon2[12]={
  };

  if(ID())
  {
    comma(4);
    while(1)
    {
      if(Serial.available())
      {
        if(i<3){
          lon1[i] = Serial.read();
        }
        if(i>=3){
          lon2[i-3] = Serial.read();
        }
        i++;
      }
      if(i==11)
      {
        i=0;

        longi1 = atof(lon1);
        longi2 = atof(lon2);

        longi1 = longi1,6 - longi2,6;
        longi3 = longi2/60;

        longi = longi1 + longi3;

        if(arahLon == 'W'){
          longi = longi * -1;
        }
        koordinatLongitude=longi;
        //        Serial.print(longi,6);
        return;
      }  
    }
  }
}

void disableGPSGPRS(){
  digitalWrite(4,HIGH);//Enable GPS mode
  digitalWrite(3,HIGH);//Disable GSM mode
  delay(1000);
}

void waitMode(){
  if(stat==3){
    disableGPSGPRS();
    stat=4;  
//    for(i=0;i<=1800;i++){
//      if(stat==4){
//        Accelero();
////        Serial.println(i);
//        delay(1000);
//      }
//    }
    stat=0;
  }
}
