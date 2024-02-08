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

import React, {Component} from 'react';
import Map from "./Map";
// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
    text: string;
    longName: string[];
    shortName: string[];
    start: string;
    end: string;
   }

class App extends Component<{}, AppState> {
    constructor(props: any) {
        super(props);
        this.state = {
            text: "",
            longName: [],
            shortName: [],
            start: "",
            end: "",
        };
    }

    buildings = async () => {
        try {
            let response = await fetch("http://localhost:4567/building-names");
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let json  = await response.json()
            this.setState({
                text: this.state.text,
                longName: Object.values(json).toString().split(","),
                shortName: Object.keys(json).toString().split(",")
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    paths = async () => {
        try {
            let startIndex = this.state.longName.indexOf(this.state.start)
            let endIndex = this.state.longName.indexOf(this.state.end)
            let response = await fetch("http://localhost:4567/shortest-path?start=" +
                                        this.state.shortName[startIndex] + "&end=" + this.state.shortName[endIndex]);
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let json  = await response.json()
            let pathValues: any = json["path"]

            if(pathValues.length === 0) {
                let startValues = json["start"]
                pathValues = []
                pathValues.push({"start": startValues, "end": startValues})
            }
            let s: string = "";
            for(let i = 0; i < pathValues.length; i++){
                let coordValues = pathValues[i]
                let startValues = coordValues["start"]
                let endValues = coordValues["end"]
                s = s + startValues["x"] +" " + startValues["y"] + " " + endValues["x"] + " " + endValues["y"] + '\n'
            }
            this.setState({
                text: s
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    selectPath = (p: string[]) =>{
        let arr: JSX.Element[] = []
        for(let i = 0; i < p.length; i++){
            let s:string = p[i]
            arr.push(<option value={s}>{s}</option>)
        }
        return arr;
    }



    handleChangeStart = (event: React.ChangeEvent<HTMLSelectElement>) =>{
        this.setState({start: event.target.value})
    }

    handleChangeEnd = (event: React.ChangeEvent<HTMLSelectElement>) =>{
        this.setState({end: event.target.value});
    }

    clear = () => {
        this.setState({
            text: ""
        })
    }

    render() {
        return(
        <div style={{textAlign:"center"}}>
            <h1 id="app-title">Line Mapper!</h1>
            <button onClick={this.buildings}>Push to Load Buildings</button>
            <div>
                <Map edges={this.state.text}/>
            </div>
            <div>
                Start:
                <select onChange={this.handleChangeStart}>
                    {this.selectPath(this.state.longName)}
                </select>
            </div>
            <div>
                End:
                <select onChange={this.handleChangeEnd}>
                    {this.selectPath(this.state.longName)}
                </select>
            </div>
            <button onClick={this.paths}>Draw</button>
            <button onClick={this.clear}>Clear</button>
        </div>
    );
    }
}

export default App;

