import './App.css';
import * as monaco from "monaco-editor";
import Editor, { DiffEditor, useMonaco, loader } from "@monaco-editor/react";
import {useCallback, useEffect, useRef, useState} from "react";
loader.config({ monaco });

function App() {
    const [code,setCode] = useState("\n");
    const [language,setLanguage] = useState("javascript")
    const [lightMode,setLightMode] = useState(true)
    useEffect(()=>{
        window.setCode = setCode;
        window.setLanguage = setLanguage;
        window.setLightMode = setLightMode;
        return ()=>{
            window.setCode = null;
            window.setLanguage = null;
            window.setLightMode = null;
        }
    },[]);
    useEffect(()=>{
        if(window?.callback?.onReady){
            window.callback.onReady()
        }
    },[])
    useEffect(()=>{
        if(window?.callback?.onCodeChanged){
            window.callback.onCodeChanged(code)
        }
    },[code])
    useEffect(()=>{
        if(window?.callback?.onLanguageChanged){
            window.callback.onLanguageChanged(code)
        }
    },[language])
    useEffect(()=>{
        if(window?.callback?.onThemeChanged){
            window.callback.onThemeChanged(lightMode)
        }
    },[lightMode])
    return (
        <Editor
          height="100%"
          defaultLanguage="javascript"
          value={code}
          onChange={setCode}
          theme={lightMode? "light":"vs-dark"}
        />
    );
}

export default App;
