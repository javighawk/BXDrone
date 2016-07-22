#define CONTROLLER_PIN        A0
#define SPEED_THRESH           5      // In percentage
#define SERIAL_BPS        115200

/* Current speed controller value */
int controller_value;


/*
 * Setup function
 */
void setup() {
    // Start serial communication
    Serial.begin(SERIAL_BPS);

    // Setup analog pin as input
    pinMode(CONTROLLER_PIN, INPUT);
}


/*
 * Loop function
 */
void loop() {
    // Read value from speed controller
    int new_value = analogRead(CONTROLLER_PIN);

    // Apply threshold
    if( new_value < 10.23 * SPEED_THRESH )
        new_value = 0;

    // Check if value has changed
    if( new_value != controller_value ){
        // Update speed value
        controller_value = new_value;

        // Get value to send
        int send_value = int(1000 * float(controller_value)/1024);

        // Send value
        Serial.write(send_value >> 8);
        Serial.write(send_value);
    }

    // Insert short delay
    delay(50);
}
