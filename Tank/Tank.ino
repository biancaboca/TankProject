#include <AFMotor.h>#include <SoftwareSerial.h>#include <Servo.h>//
#define echoPin 16 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPin 17 //attach pin D3 Arduino to pin Trig of HC-SR044
#define laser 15
#define buzzer 14
Servo turret;
AF_Stepper gun(32, 2);
SoftwareSerial bluetoothSerial(2, 3); // RX, TX
//initial motors pin
AF_DCMotor motorR(2, MOTOR12_1KHZ);
AF_DCMotor motorL(1, MOTOR12_1KHZ);
int speed;
int turretSpeed = 7;
char command1;
char command2 = 'S';
bool permission = true;
long duration; // variable for the duration of sound wave travel
int distance; // variable for the distance measurement
void setup()
{
  Serial.begin(9600);
  bluetoothSerial.begin(9600);  //Set the baud rate to your Bluetooth module.
      pinMode(buzzer, OUTPUT);
   digitalWrite(buzzer,HIGH);
  gun.setSpeed(100);
    pinMode(laser, OUTPUT);
   digitalWrite(laser,LOW);
  turret.attach(10); // attach the servo to our servo object
  turret.write(90);
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an OUTPUT
  pinMode(echoPin, INPUT); // Sets the echoPin as an INPUT
}
void loop() {
     digitalWrite(trigPin, LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin, LOW);
    duration = pulseIn(echoPin, HIGH);
    distance = duration * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
  //  Serial.print("Distance: ");
//    Serial.print(distance);
//    Serial.println(" cm");
    if(distance<=20){
      permission=false;
    }else{
      permission=true;
    }
  if (bluetoothSerial.available() > 0) {
    command2 = command1;
    command1 = bluetoothSerial.read();
    Serial.write(command1);
    if (command1 != command2)
    {
      if (command1 == '0') {
        speed = 0;
      }
      else if (command1 == '1') {
        speed = 131;
      }
      else if (command1 == '2')
      {
        speed = 230;
      }
      else if (command1 == '3')
      {
        speed = 255;
      }
      Stop();
      StopTurret();
    }
    else
    {
      switch (command1) {
        case 'Q':
          if (!permission) {
            return;
          }
          forward(speed);
          break;
        case 'W':
          if (!permission) {
            return;
          }
          UpRight(speed);
          break;
        case 'E':
          right(speed);
          break;
        case 'R':
          DownRight(speed);
          break;
        case 'T':
          back(speed);
          break;
        case 'Y':
          DownLeft(speed);
          break;
        case 'U':
          left(speed);
          break;
        case 'M':
          if (!permission) {
            return;
          }
          UpLeft(speed);
          break;
        case 'S':
          Stop();
          break;
        case 'P':
          TurretClockWise(turretSpeed + 1);
          break;
        case 'L':
          TurretCounterClockWise(turretSpeed);
          break;
        case 'A':
          StopTurret();
          break;
        case 'V':
          gunDown();
          break;
        case 'C':
          gunUp();
          break;
        case 'G':
          Shoot();
          break;
      }
    }
  }
}
void forward(int x)
{
  motorR.setSpeed(x); //Define maximum velocity
  motorR.run(FORWARD);  //rotate the motor clockwise
  motorL.setSpeed(x); //Define maximum velocity
  motorL.run(FORWARD);  //rotate the motor clockwise
}
void UpRight(int x)
{
  motorR.setSpeed(x - 100); //Define maximum velocity
  motorR.run(FORWARD);  //rotate the motor clockwise
  motorL.setSpeed(x); //Define maximum velocity
  motorL.run(FORWARD);  //rotate the motor clockwise
}
void UpLeft(int x)
{
  motorR.setSpeed(x); //Define maximum velocity
  motorR.run(FORWARD);  //rotate the motor clockwise
  motorL.setSpeed(x - 100); //Define maximum velocity
  motorL.run(FORWARD);  //rotate the motor clockwise
}
void DownRight(int x)
{
  motorR.setSpeed(x - 100); //Define maximum velocity
  motorR.run(BACKWARD);  //rotate the motor clockwise
  motorL.setSpeed(x); //Define maximum velocity
  motorL.run(BACKWARD);  //rotate the motor clockwise
}
void DownLeft(int x)
{
  motorR.setSpeed(x); //Define maximum velocity
  motorR.run(BACKWARD);  //rotate the motor clockwise
  motorL.setSpeed(x - 100); //Define maximum velocity
  motorL.run(BACKWARD);  //rotate the motor clockwise
}
void back(int x)
{
  motorR.setSpeed(x); //Define maximum velocity
  motorR.run(BACKWARD); //rotate the motor anti-clockwise
  motorL.setSpeed(x); //Define maximum velocity
  motorL.run(BACKWARD); //rotate the motor anti-clockwise
}
void left(int x)
{
  motorR.setSpeed(x); //Define maximum velocity
  motorR.run(FORWARD); //rotate the motor anti-clockwise
  motorL.setSpeed(x); //Define maximum velocity
  motorL.run(BACKWARD); //rotate the motor anti-clockwise
}
void right(int x)
{
  motorR.setSpeed(x); //Define maximum velocity
  motorR.run(BACKWARD);  //rotate the motor clockwise
  motorL.setSpeed(x); //Define maximum velocity
  motorL.run(FORWARD);  //rotate the motor clockwise
}
void Stop()
{
  motorR.setSpeed(0);  //Define minimum velocity
  motorR.run(RELEASE); //stop the motor when release the button
  motorL.setSpeed(0);  //Define minimum velocity
  motorL.run(RELEASE); //rotate the motor clockwise
  delay(10);
}
void TurretCounterClockWise(int x)
{
  turret.write(90 - x);
}
void TurretClockWise(int x)
{
  turret.write(90 + x);
}
void StopTurret()
{
  turret.write(90);
}
void gunUp() {
  gun.step(2, FORWARD, DOUBLE);
}
void gunDown() {
  gun.step(2, BACKWARD, DOUBLE);
}
void Shoot(){
  int i=3;
  while(i)
  {
  digitalWrite(laser,HIGH);
  SpaceGun(1200);
  delay(300);
    digitalWrite(laser,LOW);
  delay(300);
  i--;
  }
}
 void SpaceGun(int maximum)
{
  for(int i=0;i<maximum;i++)
  {
    digitalWrite(buzzer,HIGH);
    delayMicroseconds(i);
    digitalWrite(buzzer,LOW);
    delayMicroseconds(i);
  }
    digitalWrite(buzzer,HIGH);
}
