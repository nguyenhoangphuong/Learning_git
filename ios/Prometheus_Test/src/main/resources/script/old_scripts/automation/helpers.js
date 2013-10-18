var helper = new Helper();

function Helper()
{
	// format birthday as in profile view, input: [date, month, year]
	this.formatBirthday = function(birthday)
	{
		var monthStrings = ["dummy", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
							"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		
		return monthStrings[birthday[1]] + " " + birthday[0] + ", " + birthday[2];
	};
	
	// format height base on unit as in profile view
	this.formatHeight = function(h1, h2, isUS)
	{
		if(isUS) return h1 + "'" + h2 + "\"";
		return h1 + "." + h2 + " m";
	};
	
	// format weight base on unit as on profile view
	this.formatWeight = function(w1, w2, isUS)
	{
		return w1 + "." + w2 + (isUS ? " lbs" : " kg");
	};
	
	// format profile overview as in settings view
	this.formatProfileOverviewInfo = function(sex, h1, h2, w1, w2, isUS)
	{
		var height = isUS ? (h1 + "'" + h2 + '"') : (h1 + "." + h2 + " m");
		var weight = w1 + "." + w2 + (isUS ? " lbs" : " kg");
		
		return sex + " - " + height + " - " + weight; 
	};
	
	// format time for manual input in progress view
	this.formatTime = function(time)
	{
		return  (time[0] < 10 ? "0" + time[0] : time[0]) + ":" +
				(time[1] < 10 ? "0" + time[1] : time[0]) + ":" +
				(time[2] < 10 ? "0" + time[2] : time[0]);
	};
	
	// format time for display in progress view
	this.formatStartTime = function(timeString)
	{
		var time = timeString.split(":");
		time[0] = parseInt(time[0]);
		time[1] = parseInt(time[1]);
		
		var noon = (time[0] >= 12 ? " pm" : " am");
		var hour = time[0] % 12;
		var startTime = (hour < 10 ? "0" + hour : hour) + ":" + (time[1] < 10 ? "0" + time[1] : time[1]) + noon;
		log(startTime);
		
		return startTime;
	};
	
	// format minutes for display in progress view
	this.formatDuration = function(min)
	{
		return min + (min == 1 ? " minute" : " minutes");
	};
	
	// format points for display in progress view
	this.formatPoint = function(point)
	{
		return point + (point == 1 ? " pt" : " pts");
	};
	
	// format steps for display in progress view
	this.formatStep = function(step)
	{
		return step + (step == 1 ? " step" : " steps");
	};
	
	
}