/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
  // TODO: Define the props of this component. You will want to pass down edges
  // so you can render them here
    edges: string
}

interface MapState{}


class Map extends Component<MapProps, MapState> {

    updateMap = () => {
        let edgeArr: string[][] = this.updateArr(this.props.edges)
        let arr: JSX.Element[] = []
        for(let i = 0; i < edgeArr.length; i++){
            if(edgeArr[i].length >= 5){
                let color: string = edgeArr[i][4]
                let x1: number = parseInt(edgeArr[i][0])
                let y1: number = parseInt(edgeArr[i][1])
                let x2: number = parseInt(edgeArr[i][2])
                let y2: number = parseInt(edgeArr[i][3])
                let b1 = this.checkCoords(x1)
                let b2 = this.checkCoords(y1)
                let b3 = this.checkCoords(x2)
                let b4 = this.checkCoords(y2)
                if(b1 && b2 && b3 && b4){
                    arr.push(<MapLine key={i} color={color} x1={x1} y1={y1} x2={x2} y2={y2}/>)
                }
            }
        }
        return arr
    }

    checkCoords = (coord: number) => {
        if(isNaN(coord)){
            alert("your coordinate is not a number")
            return false
        } else if (coord < 0){
            alert("your coordinate " + coord + " is less than 0")
            return false
        } else if(coord > 4000){
            alert("your coordinate " + coord + " is greater than 4000")
            return false
        }
        return true
    }

    updateArr = (value: string) =>{
        let myArray: string[][] = []
        let newLines: string[] = value.split("\n")
        for (let i = 0; i < newLines.length; i++){
            myArray.push([]);
            let newSpace: string[] = newLines[i].split(" ")
            for(let j = 0; j < newSpace.length; j++){
                myArray[i].push(newSpace[j])
            }
        }
        return myArray
    }

  render() {
    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {this.updateMap()}
        </MapContainer>
      </div>
    );
  }
}

export default Map;
