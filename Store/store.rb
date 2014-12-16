$listItem = Hash.new
$listFailure = Hash.new

#Open file and put into a listItem, keypair is url-price
def processFile( path )
	File.readlines(path).each do |line|
		url = line.split(',').first.strip
		price = line.split(',').last.strip
		$listItem[url] = price
	end
end

#Using curl to get HTML then parse it to get price tag, finnaly compare result with current price in a Hash
#If an product has a wrong price, put it into listFailure
def checkPrice
	regEx = /itemprop="price" content="(.*?)" class/

	$listItem.each do |url, price|
		content = %x(curl #{url})
		price_store = content.scan(regEx)[0][0].to_s
		puts("^^^ #{url}, current price: #{price}, store price: #{price_store}\n\n")
		if !price_store.eql?(price)
			$listFailure[url] = price
		end
	end
end

#Showing listFailure if it is not empty
def reportResult
	puts("Wrong list: ")
	if !$listFailure.empty?
		$listFailure.each do |url, price|
			puts("#{url} has a wrong price! This price must be #{price}.")
		end
		puts("\n")
		raise "Throw an exception to trigger email sending"
	else
		puts("\nEverything is OK.")
	end
end

processFile("Store/data.csv")
checkPrice
reportResult