# Postprocessing Tool for Evolutionary Computation Symposium Competition 2019
- Author: Tatsumasa ISHIKAWA (tishikawa@flab.isas.jaxa.jp)
- Copyright (c) 2019 Tatsumasa ISHIKAWA

## 本ソースコードについて
- 進化計算シンポジウム2019コンペティションにて提出する必要があるHypervolume等のデータを出力するコードです。
- Visual Studio 2017 (Windows)及びg++ (Linux)にて動作確認をしております。
- コマンドプロンプトやターミナルでの実行を想定しております。
- 以前から配布されているR言語で書かれたスクリプトファイルと同様の仕様です。

## 利用方法
1. Visual StudioまたはMakefileを利用してコンパイルを行います。
2. コンパイルした実行ファイルを作業ディレクトリにコピーします。
3. config.jsonファイルを環境に合わせて編集します。
4. 実行ファイルを実行します。

## コマンドライン引数
- -n {number of threads} : 後処理で利用するThread数を指定します。
- -cf {filepath of configration file} : 設定ファイル(config.json)のパスを指定します。デフォルトは"config.json"です。
- -skp-ir : 実行不可能解を除去する処理をスキップします。
- -skp-sn : 解の正規化作業をスキップします。

## 設定ファイル
- R言語による処理ツールpost_windturbine_rev01.r [H. Fukumoto, T. Kohira, and T. Tatsukawa 2019]と同様の仕様です。詳しくは"config.json"ファイルを御覧ください。
- 設定例:
    - config.json
	    - "group name" : "who",
        - "number of solutions" : 100,
        - "number of generations" : 2,
        - "number of runs" : 2,
        - "digits of run" : 3,
	    - "digits of generation" : 4,
	    - "number of objectives" :5,
	    - "number of variables" : 32,
	    - "number of constraints" : 22,
	    - "filepath" : "./interface/",
	    - "run id pre" : "work_",
	    - "run id post" : "th/",
        - "variables file pre" : "gen",
        - "variables file post" : "_pop_vars_eval.txt",
	- 次のファイルが読み込まれます
	    - ./interface/work_000th/gen0000_pop_vars_eval.txt
	    - ./interface/work_000th/gen0001_pop_vars_eval.txt
	    - ./interface/work_002th/gen0000_pop_vars_eval.txt
	    - ./interface/work_002th/gen0001_pop_vars_eval.txt
	- max point, min pointを変更することで、目的関数空間正規化のための最大値最小値を変更することができます
	- reference pointを変更することで、hypervolume計算時の参照点を変更することができます(参照点は正規化した目的関数空間に対応します)