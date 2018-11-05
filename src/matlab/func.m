function func(path)
    image = imread(path);
    
    % Do stuff here
    result = image;
    
    imwrite(result, 'result.jpg', 'jpg');
end

