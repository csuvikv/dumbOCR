function func(input_path, proc_mode, output_path)
    image = imread(input_path);
    
    % Do stuff here
    result = image;
    
    imwrite(result, output_path, 'png');
end

