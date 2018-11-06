%function main2 (impathIN, task, impathOUT)
    import images.*
    
    close

    impathIN = 'test0.png';
    task = 'words';
    impathOUT = 'impathOUT1.png';
    
    I = imread(impathIN);

    %task = 'letters'; % letters/words/lines

    filter = false;
    noise = '';

    if (filter)
        noise = 'salt & pepper';
        %noise = 'gaussian';
        I = imnoise(I, noise);
    end

    size_of_disk = 10;
    SE = strel('disk', size_of_disk);

    img_bw = im2bw(I);

    img_comple = imcomplement(img_bw);

    img_to_proc = img_comple;

    % NOISE
    if (strcmp(noise, 'salt & pepper'))
        img_to_proc = medfilt2(img_comple);
    elseif (strcmp(noise, 'gaussian'))
        % just do nothing
    else
        'No noise on the picture'
    end

    % IF WORDS WHAT U WANT
    if (strcmp(task, 'words'))
        img_to_proc = imclose(img_to_proc, SE);
    end

    prop = regionprops(img_to_proc, 'BoundingBox');
    BB = cat(1, prop.BoundingBox);

%     % SHOW THE RESULT
%     if (strcmp(task, 'words') || strcmp(task, 'letters'))
%         %imshow(I);
%         for i=1:size(BB,1)
%             I(BB(i,:),:) = [255,0,0];
%             %rectangle('Position', BB(i,:),'EdgeColor','r', 'LineWidth', 1, 'LineStyle', '-');
%         end
%     end
    
%     I(round(BB(1,2)) : , round(BB(1,1)), 1) = 255;
%     I(round(BB(1,2)), round(BB(1,1)), 2) = 0;
%     I(round(BB(1,2)), round(BB(1,1)), 3) = 0;

    %h_im = imshow(I);
    %h = imrect(gca, [19.5 109.5 173 59]);
    %h = images.roi.Rectangle(gca,'Position',[500,500,1000,1000],'StripeColor','r');
    h = images.roi.rectangle();
    mask = createMask(h);
    
    
    
   % imshow(mask);
    
    if (strcmp(task, 'lines'))
        %imshow(img);
        %{
        for i=1:size(BB,1)
            rect(i) = rectangle('Position', BB(i,:),'EdgeColor','black', 'LineWidth', 1, 'LineStyle', '-');
        end
        %}

        MASK = logical(size(img_bw));
        [x,y,z] = size(I);

        for i=1:size(BB,1)
            k = i*6;
            low_left_x = BB(i,1);
            up_left_x = low_left_x;
            up_right_x = BB(i,1) + BB(i,3);
            low_right_x = up_right_x;

            low_left_y = BB(i,2);
            up_left_y = low_left_y;
            up_right_y = BB(i,2) + BB(i,4);
            low_right_y = up_right_y;

            x_rect(k-5) = low_left_x;
            x_rect(k-4) = low_right_x;
            x_rect(k-3) = up_right_x;
            x_rect(k-2) = up_left_x;
            x_rect(k-1) = low_left_x;
            x_rect(k-0) = NaN;

            y_rect(k-5) = low_left_y;
            y_rect(k-4) = up_left_y;
            y_rect(k-3) = low_right_y;
            y_rect(k-2) = up_right_y;
            y_rect(k-1) = low_left_y;
            y_rect(k-0) = NaN;
        end
        x_rect(end) = [];
        y_rect(end) = [];


        k = 1;
        for i = 1 : x
            for j = 1 : y
                yq(k) = i;
                xq(k) = j;
                k = k+1;
            end
        end

        in = inpolygon(xq, yq, x_rect, y_rect);


        MASK(yq(~in), xq(~in)) = 0;
        MASK(yq(in), xq(in)) = 1;



        % imshow(MASK);

        IR = zeros(size(I));
        [x,y,z] = size(I);


        %figure; imshow(I);

        for i = 1 : x-1
            for j = 1 : y-1
                if MASK(i,j) == 0
                    IR(i,j,:) = I(i,j,:);
                elseif MASK(i,j) == 1
                    if img_bw(i,j) == 0 %BW ben betû van
                        IR(i,j,:) = img_bw(i,j,:);
                    else
                        IR(i,j,:) = [128,128,0];
                    end
                end
            end
        end
        %figure; imshow(IR);
        imwrite(IR, impathOUT)
    end
%end