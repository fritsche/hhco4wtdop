#!/bin/bash

echo "$1" > job.log
cat job.log
batch < job.log
