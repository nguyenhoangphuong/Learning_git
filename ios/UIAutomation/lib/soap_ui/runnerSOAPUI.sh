SU_SUITE=$1
SU_CASE=$2
SU_PROJECT=$3


SU_DIR="/Applications/SmartBear/soapUI-4.5.0.app/Contents/Resources/app/bin/"




# Run test suite
function runTestSuite {
        eval sh "$SU_DIR/testrunner.sh" -s "$SU_SUITE" -c "$SU_CASE" "$SU_PROJECT"
        eval echo "complete"
}

runTestSuite
