# Postprocess Tool for Evolutionary Computation Symposium Competition 2019
- Author: Tatsumasa ISHIKAWA (tishikawa@flab.isas.jaxa.jp)
- Copyright (c) 2019 Tatsumasa ISHIKAWA

## About this code
- This code outputs data such as Hypervolume that must be submitted at the Evolutionary Computing Symposium 2019 Competition.
- This behavior has been confirmed with Visual Studio 2017 (Windows) and g ++ (Linux).
- t is assumed to be executed at the command prompt or terminal.

## Usage
1. Compile using Visual Studio or Makefile.
2. Copy the compiled executable file to the working directory.
3. Edit the config.json file to suit your environment.
4. Execute the postprocessing tool.

## Commandline options
- -n {number of threads} : Set the number of threads.
- -cf {filepath of configration file} : Set the path of the configuration file (config.json). The default is "config.json"
- -skp-ir : Skip the processing of removing the infeasible solution.
- -skp-sn : Skip solution normalization.

## Configuration file
- This code is the same specification as the processing tool by R language "post_windturbine_rev01.r". [H. Fukumoto, T. Kohira, and T. Tatsukawa 2019]
- See the file "config.json"
- Example:
    - In config.json
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
	- The following files are loaded
	    - ./interface/work_000th/gen0000_pop_vars_eval.txt
	    - ./interface/work_000th/gen0001_pop_vars_eval.txt
	    - ./interface/work_002th/gen0000_pop_vars_eval.txt
	    - ./interface/work_002th/gen0001_pop_vars_eval.txt