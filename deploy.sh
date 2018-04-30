#!/bin/bash
echo "Deploying project to Raspberries..."
echo "-----------------------------------"
echo "Cleaning project..."
echo "-----------------------------------"
rm -rf deploy/
mvn clean
echo "Building project"
echo "-----------------------------------"
mvn install
cd deploy/
echo "-----------------------------------"
echo "Creating Silo1..."
mkdir simpleSilo
cp liqueurplant.osgi.client-1.0-SNAPSHOT.jar liqueurplant.osgi.silo.driver-1.0-SNAPSHOT.jar liqueurplant.osgi.valve.* liqueurplant.osgi.silo.controller.api-1.0-SNAPSHOT.jar liqueurplant.osgi.silo.controllers.simple-1.0-SNAPSHOT.jar simpleSilo
echo "-----------------------------------"
echo "Creating mix Silo2..."
mkdir mixSilo
cp liqueurplant.osgi.client-1.0-SNAPSHOT.jar liqueurplant.osgi.silo.driver-1.0-SNAPSHOT.jar liqueurplant.osgi.valve.* liqueurplant.osgi.silo.controller.api-1.0-SNAPSHOT.jar liqueurplant.osgi.mixer* liqueurplant.osgi.silo.controllers.mix-1.0-SNAPSHOT.jar mixSilo
echo "-----------------------------------"
echo "Creating heat Silo3..."
mkdir heatSilo
cp liqueurplant.osgi.client-1.0-SNAPSHOT.jar liqueurplant.osgi.silo.driver-1.0-SNAPSHOT.jar liqueurplant.osgi.valve.* liqueurplant.osgi.silo.controller.api-1.0-SNAPSHOT.jar liqueurplant.osgi.heater* liqueurplant.osgi.silo.controllers.heat-1.0-SNAPSHOT.jar  heatSilo
echo "-----------------------------------"
echo "Creating heatmix Silo4..."
mkdir heatMixSilo
cp liqueurplant.osgi.client-1.0-SNAPSHOT.jar liqueurplant.osgi.silo.driver-1.0-SNAPSHOT.jar liqueurplant.osgi.valve.* liqueurplant.osgi.silo.controller.api-1.0-SNAPSHOT.jar liqueurplant.osgi.heater* liqueurplant.osgi.mixer* liqueurplant.osgi.silo.controllers.heatmix-1.0-SNAPSHOT.jar  heatMixSilo
