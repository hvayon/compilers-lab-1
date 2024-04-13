#!/bin/bash

dot -Tsvg graph1.dot -o graph1.svg
dot -Tsvg graph2.dot -o graph2.svg
dot -Tsvg graph3.dot -o graph3.svg
dot -Tsvg graph4.dot -o graph4.svg

open graph1.svg graph2.svg graph3.svg graph4.svg