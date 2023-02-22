import { wireTmGrammars } from 'monaco-editor-textmate';
import { Registry } from 'monaco-textmate'; // peer dependency
import { loadWASM } from 'onigasm'; // peer dependency of 'monaco-textmate'
import smaliLanguage from "./smali.tmLanguage"
export async function loadSmaliSyntax(monaco) {
    await loadWASM(require("onigasm/lib/onigasm.wasm"));
    const registry = new Registry({
        getGrammarDefinition: async (scopeName) => {
            return {
                format: "plist",
                content: smaliLanguage
            };
        }
    });

    monaco.languages.register({ id: "smali"});
    const grammars = new Map();
    grammars.set("smali", "source.smali");

    await wireTmGrammars(monaco, registry, grammars);
}